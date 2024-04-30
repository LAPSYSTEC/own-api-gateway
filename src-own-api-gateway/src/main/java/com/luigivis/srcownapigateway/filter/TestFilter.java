package com.luigivis.srcownapigateway.filter;

import com.luigivis.srcownapigateway.interfaces.OwnApiGatewayFilter;
import jakarta.servlet.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@Slf4j
public class TestFilter implements OwnApiGatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        checkValue(exchange);

        //security
        exchange.getRequest().getHeaders().forEach((key, value) -> {
            log.info(key+":"+value);
        });
        return chain.filter(exchange);
    }

    public void checkValue(ServerWebExchange exchange) {
        log.info("Exchange {}", exchange);
    }

}
