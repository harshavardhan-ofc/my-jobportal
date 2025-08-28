# ============================
# Stage 1: Build with Maven
# ============================
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests


# ============================
# Stage 2: Run lightweight JDK
# ============================
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the packaged JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Environment variables (these will be overridden by GitHub Actions/Secrets at runtime)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/jobportal \
    SPRING_DATASOURCE_USERNAME=postgres \
    SPRING_DATASOURCE_PASSWORD=password \
    APP_JWT_SECRET=defaultsecret \
    JWT_EXPIRATION=3600000

# Expose application port (change if your app runs on another port)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
