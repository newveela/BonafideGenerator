# Use official OpenJDK image with Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and project files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy rest of the project
COPY . .

# Package the app
RUN ./mvnw clean package -DskipTests

# Expose Spring Boot's default port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "target/BonafideGenerator-0.0.1-SNAPSHOT.jar"]
