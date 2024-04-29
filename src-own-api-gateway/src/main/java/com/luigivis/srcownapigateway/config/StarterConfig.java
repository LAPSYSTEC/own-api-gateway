package com.luigivis.srcownapigateway.config;

import com.luigivis.srcownapigateway.filter.TestFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class StarterConfig {

    @Bean
    public GlobalFilter customFilter() {
        return new TestFilter();
    }

}
