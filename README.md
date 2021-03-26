# Sales POC

* [Kafdrop](http://localhost:19000/)
* [Sales-Service](http://localhost:8080/swagger-ui.html)

## Como usar

#### Forma detalhada:

- Executar testes unitários
- ``` cd sales-service ```
- ``` ./gradlew clean test ```
- Reporte em ./build/reports/tests/test/index.html
<br>

- Executar testes de mutação
- ``` cd sales-service ```
- ``` ./gradlew pitest ```
- Reporte em ./build/reports/pitest/index.html
<br>

- Buildar o projeto
- ``` cd sales-service ```
- ``` ./gradlew clean build ```
<br>

- Executar projeto 
- ``` docker-compose build ```
- ``` docker-compose up ```
<br>

- Acessar swagger da aplicação: [Swagger-UI](http://localhost:8080/swagger-ui.html)
- Acessar kafdrop para visualização do tópico: [Kafdrop](http://localhost:19000/)

## Tecnologias
* **SpringBoot Webflux**: framework reativo do SpringBoot que auxilia na performance de I/O da aplicação. 
* **MongoDB**: banco de dados `schemaless` facilitando no desenvolvimento, com driver reativo já adequado para o uso do **SpringBoot Webflux**
* **Shedlock**: dependência que realiza o lock a nível de instâncias para apenas uma instância realizar um determinado job. Nesse caso a tarefa expirar a entity `assembly`
* **Apache Kafka**: plaforma de streaming de eventos amplamente utilizada em soluções corporativas, com capacidade de suportar alto throughput
* **JUnit 5**: versão atualizada do framework, com novas features facilitando a criação de testes unitários
* **Wiremock**: depencia de testes capaz de criar um server de mock para executar os testes de clients. [Wiremock Docs](http://wiremock.org/)
* **Pitest**: capaz de executar testes de mutação no código para avaliar qualidade do mesmo
* **Lombok**: plugin para auxiliar na limpeza de código dos POJO's
* **Swagger**: auxilio na documentação dos endpoints
* **Docker**: ferramenta para criar o ambiente com os containers necessários para solução proposta
* **Docker Compose**: orquestração dos containers
