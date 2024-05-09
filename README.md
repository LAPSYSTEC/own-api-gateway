# Building an API Gateway in Spring Boot

This project demonstrates how to build a basic API Gateway using Spring Boot without relying on external libraries. An API Gateway acts as a single entry point for all client requests to different services in a microservices architecture.

## Requirements

- Java JDK 21
- Maven
- Spring Boot 3+

## Steps to Follow

1. **Project Setup**

   Clone this repository to your local machine:

   ```shell
   git clone https://github.com/luigivis/own-api-gateway.git
   ```

2. **Using the library in other projects**

   Import the library in Maven or Gradle:
```xml
<dependency>
    <groupId>com.luigivis</groupId>
    <artifactId>src-own-api-gateway</artifactId>
    <version>0.1</version>
</dependency>
```
   Then use:
   `@Import(com.luigivis.ownapigateway.properties.ApiGatewayProperties.class)`

<p></p>

   as shown in the example below:

```Java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(com.luigivis.ownapigateway.properties.ApiGatewayProperties.class)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
                       
```  

3. **API Gateway Implementation**

    - `ApiGatewayApplication.java`: Main Spring Boot class.
    - `TestFilter.java`: Filter to log incoming requests.

4. **Routes Configuration**

   Define routes and their corresponding services in the `application.properties` or `application.yaml` file:

      ```properties
      # Routes Configuration
      api-gateway.filter=com.luigivis.ownapigateway.filter.TestFilter
      api-gateway.routes={name=test, to=https://api.restful-api.dev/, from=/objects/**, method=GET, POST}, {name=test1, to=https://api.restful-api.dev/, from=/objects}, {name=test2, to=https://dog.ceo/, from=/api/**}
      ```
      ```yaml
      # Routes Configuration
      api-gateway:
      filter: "com.luigivis.ownapigateway.filter.TestFilter"
      routes:
         - name: test
           to: https://api.restful-api.dev/
           from: /objects/**
           method: GET, POST
       
         - name: test1
           to: https://api.restful-api.dev/
           from: /objects
    
         - name: test2
           to: https://dog.ceo/
           from: /api/**
      ```
   This routes requests with prefix `/api1/` to the service at `http://localhost:8081`, and requests with
   prefix `/api2/` to the service at `http://localhost:8082`.
   <br>
   <br>
5. **Filter Example**
         
   To specify the filter:
   ```yaml
   api-gateway:
   filter: "com.luigivis.ownapigateway.filter.TestFilter"
   routes:
      - name: test
        to: https://api.restful-api.dev/
        from: /objects/**
   ```
   Implement the `ownapigatewayFilter` interface.

   ```java
   import com.luigivis.ownapigateway.interfaces.ownapigatewayFilter;
   import lombok.extern.slf4j.Slf4j;
   import org.springframework.cloud.gateway.filter.GatewayFilterChain;
   import org.springframework.stereotype.Service;
   import org.springframework.web.server.ServerWebExchange;
   import reactor.core.publisher.Mono;

   @Service
   @Slf4j
   public class TestFilter implements ownapigatewayFilter {
   
       @Override
       public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
   
           // Own Filter
           exchange.getRequest().getHeaders().forEach((key, value) -> {
               log.info(key + ":" + value);
           });
           return chain.filter(exchange);
       }
   
       public void checkValue(ServerWebExchange exchange) {
           log.info("Exchange {}", exchange);
       }
   
   }
   
   ```
   
   Logs if the filter was successfully loaded.
   ```text
      c.l.s.properties.ApiGatewayProperties    : Validating custom filter com.luigivis.ownapigateway.filter.TestFilter
      c.l.s.properties.ApiGatewayProperties    : Success: Found one implementation of com.luigivis.ownapigateway.filter.TestFilter as an ownapigatewayFilter
      c.l.s.properties.ApiGatewayProperties    : Filter success on load requirement com.luigivis.ownapigateway.filter.TestFilter
   ```

6. **Running the Project**

   Run the project using Maven:

   ```shell
   mvn spring-boot:run
   ```

7. **Testing the API Gateway**

   Send requests through the API Gateway to the corresponding services using the defined routes.

   For example, to access the `api1` service, send a request to `http://localhost:8080/api1/...`.

8. **Deployment to Production Environment**

   To deploy the API Gateway in a production environment, generate an executable JAR file using the following
   command:
    ```shell
    mvn package
    ```

   Then, deploy the generated JAR on your production server.

## Contributions

If you wish to contribute to this project, please follow these steps:

1. Fork the repository.
2. Create a branch for your feature: `git checkout -b feature/NewFeature`.
3. Make your changes and commit them: `git commit -am 'Add new feature'`.
4. Push your changes: `git push origin feature/NewFeature`.
5. Open a Pull Request on GitHub.

Thank you for contributing!
