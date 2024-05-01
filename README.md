# Construcción de un API Gateway en Spring Boot

Este proyecto muestra cómo construir un API Gateway básico utilizando Spring Boot sin depender de bibliotecas externas.
Un API Gateway actúa como un punto de entrada único para todas las solicitudes de clientes hacia diferentes servicios en
la arquitectura de microservicios.

## Requisitos

- Java JDK 21
- Maven
- Spring Boot 3+

## Pasos a seguir

1. **Configuración del proyecto**

   Clona este repositorio en tu máquina local:

   ```shell
   git clone https://github.com/luigivis/own-api-gateway.git`
   ```

2. **Usar libreria en otros proyectos**

   Importa la libreria en maven o gradel
```xml
<dependency>
    <groupId>com.luigivis</groupId>
    <artifactId>src-own-api-gateway</artifactId>
    <version>0.1</version>
</dependency>
```
Luego usa
`@Import(com.luigivis.srcownapigateway.properties.ApiGatewayProperties.class)`

<p></p>

como en el ejemplo de abajo

```Java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(com.luigivis.srcownapigateway.properties.ApiGatewayProperties.class)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
                       
```  

3. **Implementación del API Gateway**

    - `ApiGatewayApplication.java`: Clase principal de Spring Boot.
    - `TestFilter.java`: Filtro para registrar las solicitudes entrantes.

4. **Configuración de las rutas**

   Define las rutas y sus correspondientes servicios en el archivo `application.properties` o `application.yaml`:

      ```properties
      # Configuración de las rutas
      api-gateway.filter=com.luigivis.srcownapigateway.filter.TestFilter
      api-gateway.routes={name=test, to=https://api.restful-api.dev/, from=/objects/**, method=GET, POST}, {name=test1, to=https://api.restful-api.dev/, from=/objects}, {name=test2, to=https://dog.ceo/, from=/api/**}
      ```
      ```yaml
      # Configuración de las rutas
      api-gateway:
      filter: "com.luigivis.srcownapigateway.filter.TestFilter"
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
   Esto enruta las solicitudes con prefijo `/api1/` al servicio en `http://localhost:8081`, y las solicitudes con
   prefijo `/api2/` al servicio en `http://localhost:8082`.
   <br>
   <br>
5. **Filter Example**
         
   Para especificar el filtro:
   ```yaml
   api-gateway:
   filter: "com.luigivis.srcownapigateway.filter.TestFilter"
   routes:
      - name: test
        to: https://api.restful-api.dev/
        from: /objects/**
   ```
   Implemente la interface OwnApiGatewayFilter

   ```java
   import com.luigivis.srcownapigateway.interfaces.OwnApiGatewayFilter;
   import lombok.extern.slf4j.Slf4j;
   import org.springframework.cloud.gateway.filter.GatewayFilterChain;
   import org.springframework.stereotype.Service;
   import org.springframework.web.server.ServerWebExchange;
   import reactor.core.publisher.Mono;

   @Service
   @Slf4j
   public class TestFilter implements OwnApiGatewayFilter {
   
       @Override
       public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
   
           //Own Filter
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
   
   Logs si el filtro fue cargado exitosamente
   ```text
      c.l.s.properties.ApiGatewayProperties    : Validating custom filter com.luigivis.srcownapigateway.filter.TestFilter
      c.l.s.properties.ApiGatewayProperties    : Success: Found one implementation of com.luigivis.srcownapigateway.filter.TestFilter as a OwnApiGatewayFilter
      c.l.s.properties.ApiGatewayProperties    : Filter success on load requirement com.luigivis.srcownapigateway.filter.TestFilter
   ```

6. **Ejecución del proyecto**

   Ejecuta el proyecto utilizando Maven:

   arduino

   Copy code
    ```shell
    mvn spring-boot:run
    ```

7. **Prueba del API Gateway**

   Envía solicitudes a través del API Gateway a los servicios correspondientes utilizando las rutas definidas.

   Por ejemplo, para acceder al servicio `api1`, envía una solicitud a `http://localhost:8080/api1/...`.

8. **Despliegue en entorno de producción**

   Para desplegar el API Gateway en un entorno de producción, genera un archivo JAR ejecutable utilizando el siguiente
   comando:
    ```shell
    mvn package
    ```

   Luego, despliega el JAR generado en tu servidor de producción.

## Contribuciones

Si deseas contribuir a este proyecto, por favor sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una rama para tu funcionalidad: `git checkout -b feature/NuevaFuncionalidad`.
3. Realiza tus cambios y haz commits: `git commit -am 'Agrega nueva funcionalidad'`.
4. Sube tus cambios: `git push origin feature/NuevaFuncionalidad`.
5. Abre un Pull Request en GitHub.

¡Gracias por contribuir!
