package com.microservices.demo.consumer.logging;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Configuration
@Slf4j
public class CorrelationIdAspect {
    private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
    private static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";


    @Before(value = "execution(* com.microservices.demo.consumer.Consumer.*(..))")
    public void before(JoinPoint joinPoint) {
        var args = joinPoint.getArgs();
        final String correlationId;
        if (args.length >= 1)
            correlationId = args[args.length-1].toString();
        else
            correlationId = generateUniqueCorrelationId();
        MDC.put(CORRELATION_ID_LOG_VAR_NAME, correlationId);
    }

    @After(value = "execution(* com.microservices.demo.consumer.Consumer.*(..))")
    public void afterReturning(JoinPoint joinPoint) {
        MDC.remove(CORRELATION_ID_LOG_VAR_NAME);
    }

    private String generateUniqueCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
