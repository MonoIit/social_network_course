FROM gradle:8.9-jdk21 AS build

WORKDIR /code

COPY settings.gradle ./
COPY gradlew ./gradlew
COPY gradle ./gradle

COPY post-service ./post-service

RUN ./gradlew :post-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /code/post-service/build/libs/post-service-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
