package com.uz.redpandabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedPandaBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedPandaBackEndApplication.class, args);
    }

    @Bean
    public DataLoader dataLoader() {
        return new DataLoader();
    }

}
