package com.microservices.demo.producer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDto {
    private UUID id;
    private String status;
}