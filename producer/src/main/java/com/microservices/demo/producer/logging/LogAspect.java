package com.microservices.demo.producer.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

@Aspect
@Slf4j
@Component
public class LogAspect {
    @Around("@annotation(com.microservices.demo.producer.logging.Log)")
    public void log(ProceedingJoinPoint point) throws Throwable {
        CodeSignature codeSignature = (CodeSignature) point.getSignature();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        Logger logger = LoggerFactory.getLogger(method.getDeclaringClass());
        Log annotation = method.getAnnotation(Log.class);
        LogLevel level = annotation.logLevel();
        ChronoUnit unit = annotation.unit();
        Boolean showArgs = annotation.showArgs();
        Boolean showResult = annotation.showResult();
        Boolean showExecutionTime = annotation.showExecutionTime();
        String methodName = method.getName();
        var methodArgs = point.getArgs();
        var methodParams = codeSignature.getParameterNames();
        log(level, entry(methodName, showArgs, methodParams, methodArgs));
        var start = Instant.now();
        var response = point.proceed();
        var end = Instant.now();
        var duration = String.format("%s %s", unit.between(start, end), unit.name().toLowerCase());
        log( level, exit(methodName, duration, response, showResult, showExecutionTime));
    }

    private void log(LogLevel level , String message) {
        switch (level) {
            case DEBUG : log.debug(message);
            case TRACE : log.trace(message);
            case WARN : log.warn(message);
            case ERROR: log.error(message);
            case FATAL : log.error(message);
            default: log.info(message);
        }
    }

    private String entry(String methodName,Boolean showArgs, String[] params, Object[] args) {
        var message = new StringBuilder();
        message.append("Started ").append(methodName).append(" method")
                .append(" at ").append(new Date());
        if (showArgs && params.length>0 && args.length>0 && params.length <= args.length) {
            var values  = new HashMap<>();
            for (int i=0;i<params.length; i++) {
                values.put(params[i], args[i]);
            }
            message.append("with args:")
                    .append(values);
        }
        return message.toString();
    }

    private String exit(String methodName,String duration,Object result, Boolean showResult, Boolean showExecutionTime){
        var message = new StringBuilder();
        message.append("Finished").append(methodName).append("method");
        if (showExecutionTime) {
            message.append("in").append(duration);
        }
        if (showResult) {
            message.append("with return:").append(result.toString());
        }
        return message.toString();
    }
}
