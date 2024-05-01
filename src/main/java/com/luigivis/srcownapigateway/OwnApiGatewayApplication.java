package com.luigivis.srcownapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class OwnApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OwnApiGatewayApplication.class, args);
    }

}
