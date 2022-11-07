package com.microservices.demo.producer.repository;

import com.microservices.demo.producer.model.FoodOrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FoodOrderRequestRepository extends JpaRepository<FoodOrderRequest, UUID> {

}