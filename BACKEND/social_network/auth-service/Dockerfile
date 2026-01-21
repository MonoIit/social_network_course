FROM gradle:8.9-jdk21 AS build

WORKDIR /code

COPY settings.gradle ./
COPY gradlew ./gradlew
COPY gradle ./gradle

COPY auth-service ./auth-service

RUN ./gradlew :auth-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /code/auth-service/build/libs/auth-service-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
