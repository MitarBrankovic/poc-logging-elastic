package com.microservices.demo.consumer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, UUID> {
}