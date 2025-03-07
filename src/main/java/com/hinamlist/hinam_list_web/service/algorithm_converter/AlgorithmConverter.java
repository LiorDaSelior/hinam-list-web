package com.hinamlist.hinam_list_web.service.algorithm_converter;

import com.hinamlist.hinam_list_web.model.algorithm_converter.AlgorithmInput;
import com.hinamlist.hinam_list_web.model.algorithm_messenger.ControllerUserInput;
import com.hinamlist.hinam_list_web.service.algorithm_converter.cart_reader.ICartReader;
import com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper.ICartScraper;
import com.hinamlist.hinam_list_web.service.algorithm_converter.cart_scraper.exception.APIResponseException;
import com.hinamlist.hinam_list_web.service.algorithm_converter.translator.Translator;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class AlgorithmConverter {

    public final float priceNotFoundValue;
    protected final Map<Integer, String> storeNumberNameMap;
    protected final Map<Integer, Float> storeNumberLowerLimitMap;
    protected final Map<Integer, Float> storeNumberOrderAddonMap;

    protected final Translator translator;
    protected final Map<String, ICartScraper> cartScraperMap;
    protected final Map<String, ICartReader> cartReaderMap;

    protected final SimpleMessageConverter messageConverter;
    protected final RabbitTemplate rabbitTemplate;
    protected final String algorithmProducerExchangeName;

    // TODO: add queues support, cart readers, test translator
    @Autowired
    public AlgorithmConverter(
            @Value("${store.price-not-found-value}") float priceNotFoundValue,
            @Value("#{${store.map}}") Map<Integer, String> storeNumberNameMap,
            @Value("#{${store.lower-limit-map}}") Map<Integer, Float> storeNumberLowerLimitMap,
            @Value("#{${store.addon-map}}") Map<Integer, Float> storeNumberOrderAddonMap,
            @Value("${rabbitmq.algorithm-producer.exchange}") String algorithmProducerExchangeName,
            RabbitTemplate rabbitTemplate,
            SimpleMessageConverter messageConverter,
            Map<String, ICartScraper> cartScraperMap,
            Map<String, ICartReader> cartReaderMap,
            Translator translator
    ) {
        this.priceNotFoundValue = priceNotFoundValue;
        this.storeNumberNameMap = storeNumberNameMap;
        this.storeNumberLowerLimitMap = storeNumberLowerLimitMap;
        this.storeNumberOrderAddonMap = storeNumberOrderAddonMap;

        this.rabbitTemplate = rabbitTemplate;
        this.algorithmProducerExchangeName = algorithmProducerExchangeName;
        this.messageConverter = messageConverter;

        this.translator = translator;
        this.cartScraperMap = cartScraperMap;
        this.cartReaderMap = cartReaderMap;
    }

    @RabbitListener(queues = "${rabbitmq.converter.queue}")
    public void convertAndSendUserInput(Message message) throws IOException, InterruptedException, APIResponseException {
        ControllerUserInput controllerUserInput = (ControllerUserInput) messageConverter.fromMessage(message);

        List<String> barcodeList = new ArrayList<>(controllerUserInput.getProductAmountMap().keySet());
        Map<Integer, List<Float>> storeNumberPriceListMap = new HashMap<>();

        // for each store extract the relevant price list for each barcode in the barcodeList
        for (Integer storeNumber : storeNumberNameMap.keySet()) {

            // Using the translator (database), map each barcode (product) to the relevant StoreId for the product
            // Some barcodes might not be present in the store so they won't have a relevant StoreId and won't appear in the map
            Map<String, Integer> barcodeStoreIdMap = this.translator.translateUserInputByStore(storeNumber, controllerUserInput);

            // Map the Store id to the relevant quantity required
            Map<Integer, Float> storeIdQuantityMap = controllerUserInput.getProductAmountMap().entrySet().stream()
                    // We want to filter non-relevant barcodes (no StoreId in Store, as mentioned above)
                    .filter(stringFloatEntry -> barcodeStoreIdMap.containsKey(stringFloatEntry.getKey()))
                    .collect(
                    Collectors.toMap(
                            stringFloatEntry -> barcodeStoreIdMap.get(stringFloatEntry.getKey()),
                            Map.Entry::getValue)
                    );

            // Using cart scraper, get the cart JSON object with the relevant products (by StoreId) from the store API
            //System.out.println(cartScraperMap.keySet());
            String cartScraperName = storeNumberNameMap.get(storeNumber) + "CartScraper";
            JSONObject cartJson = cartScraperMap.get(cartScraperName).getCartObject(storeIdQuantityMap);

            // Using cart reader based on the JSON object, map each product StoreId to it's Price
            String cartReaderName = storeNumberNameMap.get(storeNumber) + "CartReader";
            // Some prices will have a NULL value, meaning they weren't in Stock
            // TODO: check product stock by database in relevant store and exclude from barcodeStoreIdMap
            Map<Integer, Float> storeIdPriceMap = cartReaderMap.get(cartReaderName).getProductIdPriceMap(cartJson);

            // We have StoreId -> Price Map, now we use it to assign a price to each barcode in barcodeList
            List<Float> priceList = barcodeList.stream()
                    // Again, some barcodes might not be present in the store so they don't have a price, they get some default value (priceNotFoundValue)
                    .map(barcode -> (
                            (barcodeStoreIdMap.containsKey(barcode) && storeIdPriceMap.containsKey(barcodeStoreIdMap.get(barcode)) ?
                            storeIdPriceMap.get(barcodeStoreIdMap.get(barcode)) : priceNotFoundValue)
                    ))
                    .toList();

            storeNumberPriceListMap.put(storeNumber, priceList);
        }

        AlgorithmInput algorithmInput = new AlgorithmInput(barcodeList, storeNumberPriceListMap, storeNumberLowerLimitMap, storeNumberOrderAddonMap);
        MessageProperties messageProperties = message.getMessageProperties();
        Message algorithmMessage = new Jackson2JsonMessageConverter().toMessage(algorithmInput,messageProperties);

        rabbitTemplate.convertAndSend(algorithmProducerExchangeName, "", algorithmMessage);
    }

}
