package com.microservices.demo.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FoodOrderService {

    private final Producer producer;
    private final FoodOrderRequestRepository repository;

    @Autowired
    public FoodOrderService(Producer producer, FoodOrderRequestRepository repository) {
        this.producer = producer;
        this.repository = repository;
    }

    public void createFoodOrder(FoodOrderDto dto) throws JsonProcessingException {
        FoodOrderRequest request = new FoodOrderRequest(dto.getItem(), dto.getAmount(), dto.getPrice());
        RequestDto requestDto = new RequestDto(request.getId(), dto.getItem(), dto.getAmount(), dto.getPrice());
        repository.save(request);
        producer.sendMessage(requestDto);
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