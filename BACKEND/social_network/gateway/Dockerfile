FROM gradle:8.9-jdk21 AS build

WORKDIR /code

COPY settings.gradle ./
COPY gradlew ./gradlew
COPY gradle ./gradle

COPY gateway ./gateway

RUN ./gradlew :gateway:bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /code/gateway/build/libs/gateway-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
