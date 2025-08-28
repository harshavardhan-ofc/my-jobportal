# ============================
# Stage 1: Build with Maven (Java 21)
# ============================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Package the application, skipping tests
RUN mvn clean package -DskipTests

# ============================
# Stage 2: Run lightweight JDK (Java 21)
# ============================
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the packaged JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set environment variables with default values,
# these can be overridden at runtime or by CI/CD pipelines.
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/jobportal \
    SPRING_DATASOURCE_USERNAME=postgres \
    SPRING_DATASOURCE_PASSWORD=password \
    APP_JWT_SECRET=defaultsecretreplaceinprod1234567890abcdefghijk \
    APP_JWT_EXPIRATION_MS=3600000

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
