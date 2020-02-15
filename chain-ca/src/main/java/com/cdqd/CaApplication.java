package com.cdqd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * CA授权验证节点
 */
@SpringBootApplication
@EnableEurekaClient
public class CaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaApplication.class, args);
    }
}
