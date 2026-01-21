package com.example.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private final static List<String> EXCLUDE_PATHS = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register"
    );

    private final WebClient webClient;
    private final String baseUrl;

    public AuthenticationFilter(@Value("${auth-service.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }


        String path = exchange.getRequest().getURI().getPath();

        if (EXCLUDE_PATHS.contains(path)) {
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("auth-token");
        if (token == null || token.isEmpty()) {
            return unauthorized(exchange, "Missing auth-token header");
        }

        return webClient.get()
                .uri("/validate")
                .header("auth-token", token)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .map(RuntimeException::new))
                .bodyToMono(ValidateResponse.class)
                .flatMap(res -> {
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("X-User-Id", res.userId.toString())
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                })
                .onErrorResume(e -> unauthorized(exchange, e.getMessage()));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(message.getBytes())));
    }

    @Override
    public int getOrder() {
        return -1; // фильтр должен выполняться до маршрутизации
    }

    public record ValidateResponse(Long userId) {}
}
