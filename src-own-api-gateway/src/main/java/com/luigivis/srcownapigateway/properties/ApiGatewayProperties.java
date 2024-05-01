package com.luigivis.srcownapigateway.properties;

import com.luigivis.srcownapigateway.interfaces.OwnApiGatewayFilter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.classreading.ClassFormatException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
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
     * Lista de rutas de la puerta de enlace de la API.
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
     * Filter full class name and location <p>com.luigivis.srcownapigateway.filter.TestFilter</p>
     * <p>Filter need <strong>implements OwnApiGatewayFilter</strong></p>
     */
    @Getter
    @Setter
    private String filter;

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
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        var builderFinal = new RouteLocatorBuilder.Builder(context);
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
            throw new IllegalStateException("Error: Multiple implementations of " + this.filter + " were found as GlobalFilters");
        }
        if (testFilterCount == 1) {
            log.info("Success: Found one implementation of {} as a OwnApiGatewayFilter", this.filter);
            validateFilter();
            return true;
        }
        if (testFilterCount == 0) {
            log.info("Filter default is disable, filter not found or filter empty");
            return true;
        }
        return true;
    }

}






