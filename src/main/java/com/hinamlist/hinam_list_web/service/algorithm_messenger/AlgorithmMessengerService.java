package com.hinamlist.hinam_list_web.service.algorithm_messenger;

import com.hinamlist.hinam_list_web.model.AlgorithmInput;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RabbitListener(queues = "${rabbitmq.algorithm-consumer.queue}")
public class AlgorithmMessengerService {
    protected static int counter = 0;
    protected RabbitTemplate rabbitTemplate;
    protected RedisTemplate<String, Object> redisTemplate;
    protected String converterExchangeName;

    @Autowired
    public AlgorithmMessengerService(RabbitTemplate rabbitTemplate,
                                     RedisTemplate<String, Object> redisTemplate,
                                     @Value("${rabbitmq.converter.exchange}") String converterExchangeName
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.redisTemplate = redisTemplate;
        this.converterExchangeName = converterExchangeName;
    }

    public void sendAlgorithmInput(AlgorithmInput algorithmInput,
                                   String sessionId) {

        Map<Object, Object> sessionAttrs = redisTemplate.opsForHash().entries(sessionId);
        sessionAttrs.put("algorithm_response", "");
        redisTemplate.opsForHash().putAll(sessionId, sessionAttrs);

        JSONObject jsonBody = createProducerMessageJson(algorithmInput.getStoreNumber(), algorithmInput.getProductAmountMap());
        String messageBody = jsonBody.toString();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setCorrelationId(sessionId);
        Message message = new Message(messageBody.getBytes(StandardCharsets.UTF_8), messageProperties);

        rabbitTemplate.convertAndSend(converterExchangeName, "", message);
    }

    protected JSONObject createProducerMessageJson(int storeNumber,
                                                   Map<String, Integer> idAmountMap) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("store_number", storeNumber);
        jsonMessage.put("products", idAmountMap);
        return jsonMessage;
    }

    @RabbitHandler
    public void receiveAlgorithmOutput(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        String sessionId = messageProperties.getCorrelationId();

        Map<Object, Object> sessionAttrs = redisTemplate.opsForHash().entries(sessionId);
        sessionAttrs.put("algorithm_response", message);
        redisTemplate.opsForHash().putAll(sessionId, sessionAttrs);
    }

    public ResponseEntity<String> getAlgorithmResult(String sessionId) {
        Map<Object, Object> sessionAttrs = redisTemplate.opsForHash().entries(sessionId);
        String res = (String) sessionAttrs.get("algorithm_response");;
        if (res == null) {
            return ResponseEntity.status(404).body("No request found!");
        }
        else if (res.isEmpty()) {
            return ResponseEntity.status(204).body("Please await result...");
        }
        return ResponseEntity.ok(res);
    }
}
