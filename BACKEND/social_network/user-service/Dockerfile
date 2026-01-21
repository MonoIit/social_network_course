FROM gradle:8.9-jdk21 AS build

WORKDIR /code

COPY settings.gradle ./
COPY gradlew ./gradlew
COPY gradle ./gradle

COPY user-service ./user-service

RUN ./gradlew :user-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /code/user-service/build/libs/user-service-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
