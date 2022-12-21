package com.example.grpcexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class GrpcexampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrpcexampleApplication.class, args);
	}

}
