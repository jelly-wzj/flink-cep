package com.roc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RocWebServer {
    public static void main(String[] args) {
        SpringApplication.run(RocWebServer.class, args);
    }
}
