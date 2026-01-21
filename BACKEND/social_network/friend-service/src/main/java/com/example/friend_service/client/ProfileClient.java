package com.example.friend_service.client;

import com.example.friend_service.dto.UserDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ProfileClient {

    private final WebClient webClient;

    public ProfileClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://user-service:8080")
                .build();
    }

    public Mono<UserDTO> getProfile(Long id) {
        return webClient.get()
                .uri("/api/v1/profile/{id}", id)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException(
                                        "Profile service error: " + body))
                )
                .bodyToMono(UserDTO.class);
    }
}
