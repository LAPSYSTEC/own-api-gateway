package com.luigivis.srcownapigateway.properties;

import com.luigivis.srcownapigateway.interfaces.OwnApiGatewayFilter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.classreading.ClassFormatException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Clase que representa las propiedades de la puerta de enlace de la API.
 * <p>
 * Estas propiedades incluyen las rutas de la puerta de enlace de la API definidas en el archivo de configuración.
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "api-gateway")
@Component
public class ApiGatewayProperties {

    @Autowired
    private ConfigurableApplicationContext context;

    /**
     * List of routes for api gateway
     * <p>
     * to: https://any.api.com/ from: /objects/**
     * <p>
     * <strong>url: https://any.api.com/objects/**</strong>
     * </p>
     */
    @Getter
    @Setter
    private List<Route> routes;

    /**
     * Filter full class name and location <p>com.luigivis.ownapigateway.filter.TestFilter</p>
     * <p>Filter need <strong>implements ownapigatewayFilter</strong></p>
     */
    @Getter
    @Setter
    private String filter;

    /**
     * The connection timeout in milliseconds.
     */
    @Getter
    @Setter
    private int connectionTimeout;

    /**
     * The response timeout in milliseconds.
     */
    @Getter
    @Setter
    private int responseTimeout;

    /**
     * Indicates whether the HTTP client should trust insecure SSL certificates.
     * Default is true.
     */
    @Getter
    @Setter
    private Boolean trustInsecureSsl = true;

    /**
     * Exposed Headers in all api gateway setting, for example you need to send <strong>Authorization</strong> from server in the header
     * exposedHeaders: JWT,
     *
     * @default '*'
     */
    @Getter
    @Setter
    private List<String> exposedHeaders = Collections.singletonList("*");

    /**
     * Request Header in all api gateway setting, is for allow header in the request for example you need allow custom header
     * requestHeaders: Authorization
     *
     * @default '*'
     */
    @Getter
    @Setter
    private List<String> requestHeaders = Collections.singletonList("*");

    @Getter
    @Setter
    private Boolean reactiveApp = true;

    private static void onApplicationEvent(ApplicationStartedEvent event) {
        try {
        } catch (BeanCreationException exception) {
            if (isSpringMvcIncompatibleException(exception)) {
                event.getSpringApplication().addListeners(new MvcCompatibilityChecker());
            }
        }
    }

    /**
     * Static class defining a route of the API Gateway.
     * <p>
     * Each route specifies the name, destination URL, origin URL, and supported HTTP method(s).
     */
    @Setter
    @Getter
    @Component
    public static class Route {
        /**
         * Name of the route.
         * <p>
         * Example: "test", "test1", "test2"
         */
        private String name;

        /**
         * URL to which the route points.
         * <p>
         * Example: "https://api.restful-api.dev/objects", "https://dog.ceo/"
         */
        private String to;

        /**
         * Origin URL of the route.
         * <p>
         * Example: "/objects", "/objects1", "/api/**"
         */
        private String from;

        /**
         * HTTP Method(s) supported by the route.
         * <p>
         * Default: GET
         * <p>
         * Supported values: GET, POST, DELETE, PUT, PATCH, OPTION
         * <p>
         * Example: GET, POST, PUT
         */
        private List<HttpMethod> method = Collections.singletonList(HttpMethod.GET);
    }

    @Bean
    public RouteLocator routeLocator() {
        var builderFinal = new RouteLocatorBuilder.Builder(context);
        if (ObjectUtils.isEmpty(this.routes)) return builderFinal.build();
        for (Route route : this.getRoutes()) {
            log.info("Setting ApiGateway name: {} to: {} from: {} method: {}", route.getName(), route.getTo(), route.getFrom(), route.getMethod());
            var httpMethodsArray = route.getMethod().toArray(new HttpMethod[0]);
            builderFinal = builderFinal.route(route.getName(),
                    r -> r.path(route.getFrom())
                            .and()
                            .method(httpMethodsArray)
                            .uri(route.getTo())
            );
        }
        return builderFinal.build();
    }

    @Bean
    public CorsWebFilter corsFilter() {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        corsConfiguration.setAllowedHeaders(requestHeaders);
        corsConfiguration.setExposedHeaders(exposedHeaders);
        corsConfiguration.addAllowedHeader("accept");
        corsConfiguration.addAllowedHeader("authorization");
        corsConfiguration.addAllowedHeader("cookie");
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }

    @Bean
    @Primary
    public HttpClientProperties httpClientPropertiesCustom() {
        log.info("Setting http client properties connectionTimeout {} responseTimeout {} trustInsecureSsl {}"
                , connectionTimeout, responseTimeout, trustInsecureSsl);
        var http = new HttpClientProperties();

        if (ObjectUtils.isNotEmpty(this.connectionTimeout))
            http.setConnectTimeout(this.connectionTimeout);

        if (ObjectUtils.isNotEmpty(this.responseTimeout))
            http.setResponseTimeout(Duration.ofSeconds(this.responseTimeout));

        var ssl = new HttpClientProperties.Ssl();
        ssl.setUseInsecureTrustManager(this.trustInsecureSsl);
        http.setSsl(ssl);
        return http;
    }

    public void validateFilter() throws ClassFormatException {
        try {
            var filterClass = Class.forName(this.filter);

            if (!OwnApiGatewayFilter.class.isAssignableFrom(filterClass) && Modifier.isAbstract(filterClass.getModifiers())) {
                throw new ClassFormatException("Error: Class new implements OwnApiGatewayFilter and can't be Abstract");
            }
            log.info("Filter success on load requirement {}", this.filter);


        } catch (ClassNotFoundException e) {
            log.info("Error: Class not found \n" + e);
        }
    }

    @Bean
    @Order(0)
    public Boolean validateUniqueFilter(ApplicationContext applicationContext) throws ClassFormatException {
        if (StringUtils.isBlank(this.filter)) {
            log.info("Filter default is disable, filter not found or filter empty");
            return false;
        }

        log.info("Validating custom filter {}", this.filter);
        var globalFilterBeans = applicationContext.getBeansOfType(OwnApiGatewayFilter.class);

        var testFilterCount = 0;
        for (OwnApiGatewayFilter ignored : globalFilterBeans.values()) {
            testFilterCount++;
        }

        if (testFilterCount > 1) {
            throw new IllegalStateException("Error: Multiple implementations of were found as ownapigatewayFilter");
        }
        if (testFilterCount == 1) {
            log.info("Success: Found one implementation of {} as a ownapigatewayFilter", this.filter);
            validateFilter();
            return true;
        }
        if (testFilterCount == 0) {
            log.info("Filter default is disable, filter not found or filter empty");
            return true;
        }
        return true;
    }

    @Bean
    public ApplicationListener<ApplicationStartedEvent> applicationListener() {
        return ApiGatewayProperties::onApplicationEvent;
    }

    @Bean
    public WebApplicationType webApplicationType() {
        return switch (this.reactiveApp.toString()) {
            case "true" -> WebApplicationType.REACTIVE;
            case "false" -> WebApplicationType.SERVLET;
            default -> WebApplicationType.NONE;
        };
    }

    static class MvcCompatibilityChecker implements ApplicationListener<ApplicationFailedEvent> {
        @Override
        public void onApplicationEvent(ApplicationFailedEvent event) {
            var throwable = event.getException();
            if (throwable != null && isSpringMvcIncompatibleException(throwable)) {
                System.err.println("Spring MVC found on classpath, which is incompatible with Own Api Gateway.");
                System.err.println("Please set api-gateway.reactive=true or remove spring-boot-starter-web dependency.");
            }
        }
    }

    protected static boolean isSpringMvcIncompatibleException(Throwable throwable) {
        String message = throwable.getMessage();
        return message != null && message.contains("Spring MVC found on classpath");
    }

}






