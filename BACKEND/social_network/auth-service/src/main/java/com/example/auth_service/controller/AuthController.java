package com.example.auth_service.controller;

import com.example.auth_service.dto.*;
import com.example.auth_service.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        System.out.println("register");
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthOkResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("X-User-Id") Long userId) {
        authService.logout(userId);
    }

    @DeleteMapping("/account")
    public void account(@RequestHeader("X-User-Id") Long userId) {
        authService.delete(userId);
    }

    @GetMapping("/validate")
    public ValidateResponse validateToken(@RequestHeader("auth-token") String token) {
        return authService.validate(token);
    }

}
