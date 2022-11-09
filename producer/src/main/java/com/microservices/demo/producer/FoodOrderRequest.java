package com.microservices.demo.producer;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodOrderRequest {
    @Id
    private UUID id = UUID.randomUUID();
    @Column
    private String item;
    @Column
    private Double amount;
    @Column
    private Double price;
    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public FoodOrderRequest(String item, Double amount, Double price) {
        this.item = item;
        this.amount = amount;
        this.price = price;
        this.status = OrderStatus.PROCESSING;
    }
}