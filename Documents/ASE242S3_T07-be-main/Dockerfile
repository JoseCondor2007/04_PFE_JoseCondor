# Stage 1: Build with Maven
FROM maven:3.9-amazoncorretto-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run with Java
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


# docker run -d --name sqlserver -p 1433:1433 araceli09/sql-server:2022

# docker build -t araceli09/back-end:1.0 .

# docker run -d --name back-end -p 8085:8085 araceli09/back-end:1.0

# docker push araceli09/back-end:1.0

# docker push araceli09/sql-server:2022