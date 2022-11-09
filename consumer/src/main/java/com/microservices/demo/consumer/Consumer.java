package com.microservices.demo.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private static final String orderTopic = "${topic.name}";
    private static final Logger log = LogManager.getLogger("elasticsearch");
    private final ObjectMapper objectMapper;
    private final FoodOrderService foodOrderService;

    @Autowired
    public Consumer(ObjectMapper objectMapper, FoodOrderService foodOrderService) {
        this.objectMapper = objectMapper;
        this.foodOrderService = foodOrderService;
    }

    @KafkaListener(topics = orderTopic, groupId = "orders", containerFactory = "kafkaJsonListenerContainerFactory")
    public void consumeMessage(RequestDto requestDto,
                               @Header("correlation_id") String correlationID) throws JsonProcessingException {
        log.info("message consumed {}", requestDto);
        log.info("correlationID {}", correlationID);

        //RequestDto requestDto = objectMapper.readValue(message, RequestDto.class);
        FoodOrder foodOrder = new FoodOrder(requestDto.getId(), requestDto.getItem(), requestDto.getAmount(), requestDto.getPrice());
        foodOrderService.persistFoodOrder(foodOrder, requestDto.getReplyChannel());
    }

}