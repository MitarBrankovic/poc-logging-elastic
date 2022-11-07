package com.microservices.demo.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    private static final String orderTopic = "${topic.name}";
    private final ObjectMapper objectMapper;
    private final FoodOrderService foodOrderService;

    @Autowired
    public Consumer(ObjectMapper objectMapper, FoodOrderService foodOrderService) {
        this.objectMapper = objectMapper;
        this.foodOrderService = foodOrderService;
    }

    @KafkaListener(topics = orderTopic, groupId = "orders")
    public void consumeMessage(String message) throws JsonProcessingException {
        log.info("message consumed {}", message);
        RequestDto requestDto = objectMapper.readValue(message, RequestDto.class);
        FoodOrder foodOrder = new FoodOrder(requestDto.getId(), requestDto.getItem(), requestDto.getAmount(), requestDto.getPrice());
        foodOrderService.persistFoodOrder(foodOrder, requestDto.getReplyChannel());
    }

}