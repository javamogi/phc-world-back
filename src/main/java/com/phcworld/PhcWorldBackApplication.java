package com.phcworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PhcWorldBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhcWorldBackApplication.class, args);
    }

}
