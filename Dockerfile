# Use a lightweight Java base image
FROM maven:3.8-openjdk-17-slim  AS builder

WORKDIR /app

COPY ./src  ./src/
COPY ./pom.xml .

# Install dependencies using Maven
RUN mvn clean install 

# Create a non-root user for improved security
FROM openjdk:17-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Set working directory for the application
WORKDIR /app

# Copy the application JAR file (assuming it's located in target/)
COPY --from=builder /app/target/leave-manager-1.0.0-SNAPSHOT.jar app.jar

# Expose the Spring Boot application port (adjust based on your application)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]