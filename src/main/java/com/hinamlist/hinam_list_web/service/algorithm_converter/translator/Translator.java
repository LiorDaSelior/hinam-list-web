package com.hinamlist.hinam_list_web.service.algorithm_converter.translator;

import com.hinamlist.hinam_list_web.model.algorithm_converter.translator.MainTableProduct;
import com.hinamlist.hinam_list_web.model.algorithm_messenger.ControllerUserInput;
import com.hinamlist.hinam_list_web.repository.algorithm_converter.MainTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Translator implements ITranslator {

    protected final MainTableRepository repo;

    @Autowired
    public Translator(MainTableRepository repo) {
        this.repo = repo;
    }

    @Override
    public Map<String, Integer> translateUserInputByStore(int storeNumber, ControllerUserInput controllerUserInput) {
        return this.repo.findProductListByStoreNumberAndBarcodeList(storeNumber,
                        controllerUserInput.getProductAmountMap().keySet().stream().toList()
                )
                .stream()
                .collect(
                        Collectors.toMap(
                                MainTableProduct::getBarcode,
                                MainTableProduct::getStoreId
                        )
                );
    }
}
