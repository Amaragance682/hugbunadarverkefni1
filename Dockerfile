FROM maven:3.9.11-eclipse-temurin-25 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:25-ea-jdk-slim
COPY --from=build /target/clock-in-0.0.1-SNAPSHOT.jar clock-in.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "clock-in.jar"]
