package com.microservices.demo.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FoodOrderService {

    private final FoodOrderRepository foodOrderRepository;

    private Double balance = 2000.0;
    private final Producer producer;

    @Autowired
    public FoodOrderService(FoodOrderRepository foodOrderRepository, Producer producer) {
        this.foodOrderRepository = foodOrderRepository;
        this.producer = producer;
    }

    public void persistFoodOrder(FoodOrder foodOrder, String replyChannel) throws JsonProcessingException {
        Double money = foodOrder.getPrice() * foodOrder.getAmount();
        OrderStatus status = balance - money >= 0 ? OrderStatus.CONFIRMED : OrderStatus.CANCELLED;
        if (status.equals(OrderStatus.CONFIRMED)) {
            this.deduct(money);
        }
        foodOrder.setStatus(status);
        FoodOrder persistedFoodOrder = foodOrderRepository.save(foodOrder);
        producer.sendMessage(new ResponseDto(foodOrder.getId(), status.toString()), replyChannel);
        log.info("food order persisted {}", persistedFoodOrder);
        log.info("count: {}", foodOrderRepository.findAll().size());
    }

    private void deduct(Double money) {
        this.balance = this.balance - money;
    }
}