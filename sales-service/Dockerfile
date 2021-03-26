FROM openjdk:11
ADD /build/libs/sales-service-*.jar /sales-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/sales-service.jar"]