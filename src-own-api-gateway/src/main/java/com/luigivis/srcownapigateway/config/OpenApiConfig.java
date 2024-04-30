package com.luigivis.srcownapigateway.config;

import com.luigivis.srcownapigateway.properties.ApiGatewayProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private final ApiGatewayProperties apiGatewayProperties;

    public OpenApiConfig(ApiGatewayProperties apiGatewayProperties) {
        this.apiGatewayProperties = apiGatewayProperties;
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder().group("user").pathsToExclude("/api/v2/**").pathsToMatch("/api/v1/**").build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder().group("admin").pathsToExclude("/api/v1/**").pathsToMatch("/api/v2/**").build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("own-api-gateway")
                        .description("ApiGateway more easy, swagger, telemetry included")
                        .contact(new Contact()
                                .name("Luigi Vismara")
                                .email("luigivis98@gmail.com")
                                .url("https://github.com/luigivis"))
                );
    }

}
