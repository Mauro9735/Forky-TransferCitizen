# Etapa de build con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

# Etapa de producción con JDK 21

FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia el JAR desde la etapa de build

COPY --from=build /app/target/*.jar app.jar

# Expone el puerto 8080

EXPOSE 8080

# Comando para ejecutar la app

CMD ["java", "-jar", "app.jar"]

