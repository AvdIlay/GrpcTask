package com.example.grpcexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Handler {
    @Bean
    public ConcurrentHashMap<Long, Long> rpcHandler(){
        return new ConcurrentHashMap<>();
    }
}
