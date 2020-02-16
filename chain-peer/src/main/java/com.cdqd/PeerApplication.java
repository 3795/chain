package com.cdqd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEurekaClient
@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class PeerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeerApplication.class, args);
    }
}
