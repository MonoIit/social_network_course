package com.example.auth_service.service;

import com.example.auth_service.dto.*;

public interface IAuthService {

    public RegisterResponse register(RegisterRequest request);

    public AuthOkResponse login(LoginRequest request);

    public void logout(Long id);

    public void delete(Long id);

    public ValidateResponse validate(String token);

}
