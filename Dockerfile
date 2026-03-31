# Estágio 1: Build
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Execução
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/poo-acme-voting-*.jar app.jar
COPY input.txt .

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
