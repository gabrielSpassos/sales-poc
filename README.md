# Sales POC

* [Kafdrop](http://localhost:19000/)
* [Sales-Service](http://localhost:8080/swagger-ui.html)

## How to use

#### Simple way:

 - ``` chmod +x start.sh ```
 - ``` sh start.sh ```

#### Detail way:

- Execute unit tests
- ``` cd sales-service ```
- ``` ./gradlew clean test ```
- Report at ./build/reports/tests/test/index.html
<br>

- Execute mutation tests
- ``` cd sales-service ```
- ``` ./gradlew pitest ```
- Report at ./build/reports/pitest/index.html
<br>

- Build project
- ``` cd sales-service ```
- ``` ./gradlew clean build ```
<br>

- Run project 
- ``` docker-compose build ```
- ``` docker-compose up ```
<br>

- Application Swagger: [Swagger-UI](http://localhost:8080/swagger-ui.html)
- Kafdrop to access broker and topic: [Kafdrop](http://localhost:19000/)

## Technologies
* **SpringBoot Webflux**: SpringBoot reactive framework to increase I/O performance. 
* **MongoDB**: `schemaless` database with reactive driver to work with **SpringBoot Webflux**
* **Shedlock**: dependency to create multi instance lock at database, used at sales analysis scheduler 
* **Apache Kafka**: streaming events platafrom, with high throughput support
* **JUnit 5**: new version of test framework, has some new features to help at unit tests
* **Wiremock**: dependency that creates mock servers to test the clients, used the [Mock Labs](https://get.mocklab.io/) to create the external clients for validations. [Wiremock Docs](http://wiremock.org/)
* **Pitest**: dependency to use at mutation tests
* **Lombok**: development plugin
* **Swagger**: endpoints documentation
* **Docker**: tool used to create the enviroment of the solution with all infraestructure dependencies 
* **Docker Compose**: containers orchestration
