package com.microservices.demo.consumer;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "food_orders")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FoodOrder {

    @Id
    private UUID id;
    @Column
    private String item;
    @Column
    private Double amount;
    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column
    private Double price;

    public FoodOrder(UUID id, String item, Double amount, Double price) {
        this.id = id;
        this.item = item;
        this.amount = amount;
        this.price = price;
        this.status = OrderStatus.PROCESSING;
    }
}