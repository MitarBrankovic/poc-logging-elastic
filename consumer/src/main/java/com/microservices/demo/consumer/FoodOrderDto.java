package com.microservices.demo.consumer;
import lombok.Data;
import lombok.Value;
import java.util.UUID;

@Data
@Value
public class FoodOrderDto {
    UUID id;
    String item;
    Double amount;
    Double price;
}