package com.microservices.demo.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class Consumer {

    private final ObjectMapper objectMapper;
    private static final String requestsTopic = "${requests.topic.name}";
    private final FoodOrderService service;

    public Consumer(ObjectMapper objectMapper, FoodOrderService service) {
        this.objectMapper = objectMapper;
        this.service = service;
    }

    @KafkaListener(topics = requestsTopic, groupId = "requests")
    @Transactional
    public void consumeMessage(String message) throws JsonProcessingException {
        log.info("message consumed {}", message);
        ResponseDto responseDto = objectMapper.readValue(message, ResponseDto.class);
        OrderStatus status = responseDto.getStatus().equals("CONFIRMED") ? OrderStatus.CONFIRMED : OrderStatus.CANCELLED;
        service.update(responseDto.getId(), status);
    }
}
