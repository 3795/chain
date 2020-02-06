package com.cdqd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class PeerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeerApplication.class, args);
    }
}
