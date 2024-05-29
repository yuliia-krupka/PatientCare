package com.pcare.pcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PCareApplication {

    public static void main(String[] args) {
        SpringApplication.run(PCareApplication.class, args);
    }

}
