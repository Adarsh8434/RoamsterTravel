package com.roamster.clothing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ClothingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClothingServiceApplication.class, args);
    }
}
