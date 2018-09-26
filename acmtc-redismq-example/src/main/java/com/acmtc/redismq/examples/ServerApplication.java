package com.acmtc.redismq.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@EntityScan("com.acmtc")
@SpringBootApplication(scanBasePackages = {"com.acmtc"})
public class ServerApplication {
	public static void main(String[] args) {
		
		SpringApplication.run(ServerApplication.class, args);
		
	}
} 
