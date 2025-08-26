# Use official Maven image with Java 17 to build the project
FROM maven:3.9.10-jdk-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies (will be cached if unchanged)
COPY pom.xml .

RUN mvn dependency:go-offline

# Copy the source code to the container
COPY src ./src

# Package the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# Use a lightweight OpenJDK 21 image for the runtime
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on (usually 8080)
EXPOSE 8080

# Command to run the jar file
ENTRYPOINT ["java","-jar","app.jar"]
