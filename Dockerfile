# Use the official OpenJDK 17 image as a base image
FROM openjdk:17-jdk-slim


LABEL authors="sanju"

WORKDIR /app


COPY target/bonafidegenerator.jar /app/bonafidegenerator.jar


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "/app/bonafidegenerator.jar"]
