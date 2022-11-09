package com.microservices.demo.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FoodOrderService {

    private final Producer producer;
    private final FoodOrderRequestRepository repository;
    private final OutboxItemRepository outboxItemRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public FoodOrderService(Producer producer, FoodOrderRequestRepository repository, OutboxItemRepository outboxItemRepository, ObjectMapper objectMapper) {
        this.producer = producer;
        this.repository = repository;
        this.outboxItemRepository = outboxItemRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void createFoodOrder(FoodOrderDto dto) throws JsonProcessingException {
        FoodOrderRequest request = new FoodOrderRequest(dto.getItem(), dto.getAmount(), dto.getPrice());
        RequestDto requestDto = new RequestDto(request.getId(), dto.getItem(), dto.getAmount(), dto.getPrice());
        repository.save(request);
        OutboxItem outboxItem = OutboxItem.builder().payload(objectMapper.writeValueAsString(requestDto)).type("Request made").timestamp(new Date()).aggregateType("request").aggregateId(UUID.randomUUID().toString()).correlationId(UUID.randomUUID().toString()).build();
        outboxItemRepository.save(outboxItem);
    }

    public List<FoodOrderRequest> getRequests() {
        return repository.findAll();
    }

    public void update(UUID id, OrderStatus status) {
        FoodOrderRequest old = repository.getById(id);
        old.setStatus(status);
        repository.save(old);
    }
}