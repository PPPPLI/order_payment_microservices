package com.cloud.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.cloud.spring.repository"})
@EnableDiscoveryClient
public class Main8084 {
    public static void main(String[] args) {

        SpringApplication.run(Main8084.class,args);
    }
}