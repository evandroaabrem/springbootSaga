package com.example.sbkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SbKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SbKafkaApplication.class, args);
    }
}

