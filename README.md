# Construcción de un API Gateway en Spring Boot

Este proyecto muestra cómo construir un API Gateway básico utilizando Spring Boot sin depender de bibliotecas externas. Un API Gateway actúa como un punto de entrada único para todas las solicitudes de clientes hacia diferentes servicios en la arquitectura de microservicios.

## Requisitos

-   Java JDK 21
-   Maven
- Spring Boot 3+

## Pasos a seguir

1.  **Configuración del proyecto**
    
    Clona este repositorio en tu máquina local:
    
    bash
    
    Copy code
    
    `git clone https://github.com/luigivis/own-api-gateway.git` 
    
2.  **Estructura del proyecto**
    
    El proyecto sigue una estructura básica de Spring Boot:
    
```text
    `src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── ejemplo
    │   │           └── apigateway
    │   │               ├── ApiGatewayApplication.java
    │   │               ├── controller
    │   │               │   └── GatewayController.java
    │   │               ├── filter
    │   │               │   └── LoggingFilter.java
    │   │               └── route
    │   │                   └── Route.java
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── com
                └── ejemplo
                    └── apigateway
                        └── ApiGatewayApplicationTests.java
                        
```  
    
3.  **Implementación del API Gateway**
    
    -   `ApiGatewayApplication.java`: Clase principal de Spring Boot.
    -   `GatewayController.java`: Controlador que maneja las solicitudes entrantes y enruta a los servicios correspondientes.
    -   `LoggingFilter.java`: Filtro para registrar las solicitudes entrantes.
    -   `Route.java`: Clase para definir las rutas y sus correspondientes servicios.
4.  **Configuración de las rutas**
    
    Define las rutas y sus correspondientes servicios en el archivo `application.properties`:
    
    ```properties
    
    # Configuración de las rutas
    routes.api1.path=/api1/**
    routes.api1.url=http://localhost:8081
    
    routes.api2.path=/api2/**
    routes.api2.url=http://localhost:8082` 
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
