FROM gradle:8.9-jdk21 AS build

WORKDIR /code

COPY settings.gradle ./
COPY gradlew ./gradlew
COPY gradle ./gradle

COPY messenger-service ./messenger-service

RUN ./gradlew :messenger-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /code/messenger-service/build/libs/messenger-service-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
