package com.roamster.photo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PhotoServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhotoServiceApplication.class, args);
    }
}
