package com.microservices.demo.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
public class FoodOrderController {

    private final FoodOrderService foodOrderService;

    @Autowired
    public FoodOrderController(FoodOrderService foodOrderService) {
        this.foodOrderService = foodOrderService;
    }

    @PostMapping
    public void createFoodOrder(@RequestBody FoodOrderDto dto) throws JsonProcessingException {
        log.info("create food order request received");
        foodOrderService.createFoodOrder(dto);
    }

    @GetMapping
    public ResponseEntity<?> getOrderRequests() {
        return ResponseEntity.ok(foodOrderService.getRequests());
    }
}