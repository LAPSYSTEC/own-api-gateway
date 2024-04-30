# Construcción de un API Gateway en Spring Boot

Este proyecto muestra cómo construir un API Gateway básico utilizando Spring Boot sin depender de bibliotecas externas. Un API Gateway actúa como un punto de entrada único para todas las solicitudes de clientes hacia diferentes servicios en la arquitectura de microservicios.

## Requisitos

-   Java JDK 21
-   Maven
- Spring Boot 3+

## Pasos a seguir

1.  **Configuración del proyecto**
    
    Clona este repositorio en tu máquina local:

    ```shell
    git clone https://github.com/luigivis/own-api-gateway.git`
    ```
    
2.  **Estructura del proyecto**
    
    El proyecto sigue una estructura básica de Spring Boot:
    
```text
    `src
    ├── main
       ├── java
       │   └── com
       │       └── ejemplo
       │           └── apigateway
       │               ├── ApiGatewayApplication.java
       │               ├── controller
       │               │   └── GatewayController.java
       │               ├── filter
       │               │   └── LoggingFilter.java
       │               └── route
       │                   └── Route.java
       └── resources
           └── application.properties
                        
```  
    
3.  **Implementación del API Gateway**
    
    -   `ApiGatewayApplication.java`: Clase principal de Spring Boot.
    -   `TestFilter.java`: Filtro para registrar las solicitudes entrantes.
 
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
      Esto enruta las solicitudes con prefijo `/api1/` al servicio en `http://localhost:8081`, y las solicitudes con prefijo `/api2/` al servicio en `http://localhost:8082`.
    
5.  **Ejecución del proyecto**
    
    Ejecuta el proyecto utilizando Maven:
    
    arduino
    
    Copy code
    ```shell
    mvn spring-boot:run
    ```
    
6.  **Prueba del API Gateway**
    
    Envía solicitudes a través del API Gateway a los servicios correspondientes utilizando las rutas definidas.
    
    Por ejemplo, para acceder al servicio `api1`, envía una solicitud a `http://localhost:8080/api1/...`.
    
7.  **Despliegue en entorno de producción**
    
    Para desplegar el API Gateway en un entorno de producción, genera un archivo JAR ejecutable utilizando el siguiente comando:
    ```shell
    mvn package
    ```
    
    Luego, despliega el JAR generado en tu servidor de producción.
    

## Contribuciones

Si deseas contribuir a este proyecto, por favor sigue estos pasos:

1.  Haz un fork del repositorio.
2.  Crea una rama para tu funcionalidad: `git checkout -b feature/NuevaFuncionalidad`.
3.  Realiza tus cambios y haz commits: `git commit -am 'Agrega nueva funcionalidad'`.
4.  Sube tus cambios: `git push origin feature/NuevaFuncionalidad`.
5.  Abre un Pull Request en GitHub.

¡Gracias por contribuir!
