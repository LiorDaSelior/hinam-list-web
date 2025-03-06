package com.hinamlist.hinam_list_web.service.algorithm_messenger;

import com.hinamlist.hinam_list_web.model.algorithm_messenger.ControllerUserInput;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service

public class AlgorithmMessenger {
    protected RabbitTemplate rabbitTemplate;
    protected RedisTemplate<String, Object> redisTemplate;
    protected String converterExchangeName;
    protected final SimpleMessageConverter messageConverter;

    @Autowired
    public AlgorithmMessenger(RabbitTemplate rabbitTemplate,
                              RedisTemplate<String, Object> redisTemplate,
                              @Value("${rabbitmq.converter.exchange}") String converterExchangeName,
                              SimpleMessageConverter messageConverter
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.redisTemplate = redisTemplate;
        this.converterExchangeName = converterExchangeName;
        this.messageConverter = messageConverter;
    }

    public void sendAlgorithmInput(ControllerUserInput controllerUserInput,
                                   String sessionId) {

        Map<Object, Object> sessionAttrs = redisTemplate.opsForHash().entries(sessionId);
        sessionAttrs.put("algorithm_response", "");
        redisTemplate.opsForHash().putAll(sessionId, sessionAttrs);

        //JSONObject jsonBody = createProducerMessageJson(controllerUserInput.getStoreNumber(), controllerUserInput.getProductAmountMap());
        //String messageBody = jsonBody.toString();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setCorrelationId(sessionId);
        Message message = new SimpleMessageConverter().toMessage(controllerUserInput,messageProperties);

        rabbitTemplate.convertAndSend(converterExchangeName, "", message);
    }

    protected JSONObject createProducerMessageJson(int storeNumber,
                                                   Map<String, Integer> idAmountMap) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("store_number", storeNumber);
        jsonMessage.put("products", idAmountMap);
        return jsonMessage;
    }

    @RabbitListener(queues = "${rabbitmq.algorithm-consumer.queue}")
    public void receiveAlgorithmOutput(Message message) {
        Map<String, Integer> algorithmOutput = (Map<String, Integer>) messageConverter.fromMessage(message);
        MessageProperties messageProperties = message.getMessageProperties();
        String sessionId = messageProperties.getCorrelationId();

        Map<Object, Object> sessionAttrs = redisTemplate.opsForHash().entries(sessionId);
        sessionAttrs.put("algorithm_response", algorithmOutput);
        redisTemplate.opsForHash().putAll(sessionId, sessionAttrs);
    }

    public ResponseEntity<String> getAlgorithmResult(String sessionId) {
        Map<Object, Object> sessionAttrs = redisTemplate.opsForHash().entries(sessionId);
        var result = sessionAttrs.get("algorithm_response");
        if (result == null) {
            return ResponseEntity.status(404).body("No request found!");
        }
        String resultString = sessionAttrs.get("algorithm_response").toString();
        if (resultString.isEmpty()) {
            return ResponseEntity.status(204).body("Please await result...");
        }
        Map<String, Integer> resultObject = (Map<String, Integer>) sessionAttrs.get("algorithm_response");
        String resultJsonString = new JSONObject(resultObject).toString();
        return ResponseEntity.ok(resultJsonString);
    }
}
