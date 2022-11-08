package com.microservices.demo.consumer;
import com.microservices.demo.consumer.logging.CorrelationInterceptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.servlet.handler.MappedInterceptor;

@EnableKafka
@Configuration
public class Config {

    @Autowired
    CorrelationInterceptor c;
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public MappedInterceptor myInterceptor()
    {
        return new MappedInterceptor(null, c);
    }

}