package com.cloud.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@ServletComponentScan(basePackages = {"com.cloud.spring.filter"})
public class Main8082 {
    public static void main(String[] args) {

        SpringApplication.run(Main8082.class,args);
    }
}