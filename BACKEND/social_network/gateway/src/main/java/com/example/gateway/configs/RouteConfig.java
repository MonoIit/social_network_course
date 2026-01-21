package com.example.gateway.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
//                // Аутентификация (не требует токена)
//                .route("auth", r -> r.path("/api/v1/auth/**")
//                        .uri("http://localhost:8081"))
//
//                // Профили
//                .route("profile", r -> r.path("/api/v1/profile/**")
//                        .uri("http://localhost:8082"))
//
//                // Друзья
//                .route("friends", r -> r.path("/api/v1/friends/**")
//                        .uri("http://localhost:8083"))
//
//                // Мессенджер
//                .route("messenger", r -> r.path("/api/v1/messenger/**")
//                        .uri("http://localhost:8084"))
//
//                // Лента
//                .route("feed", r -> r.path("/api/v1/posts/**")
//                        .uri("http://localhost:8085"))
                .route("auth", r -> r.path("/api/v1/auth/**")
                        .uri("http://auth-service:8080"))

                .route("profile", r -> r.path("/api/v1/profile/**")
                        .uri("http://user-service:8080"))

                .route("friends", r -> r.path("/api/v1/friends/**")
                        .uri("http://friend-service:8080"))

                .route("messenger", r -> r.path("/api/v1/messenger/**")
                        .uri("http://messenger-service:8080"))

                .route("feed", r -> r.path("/api/v1/posts/**")
                        .uri("http://post-service:8080"))


                .build();
    }
}
