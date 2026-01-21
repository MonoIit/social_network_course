FROM gradle:8.9-jdk21 AS build

WORKDIR /code

COPY settings.gradle ./
COPY gradlew ./gradlew
COPY gradle ./gradle

COPY friend-service ./friend-service

RUN ./gradlew :friend-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /code/friend-service/build/libs/friend-service-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
