package com.example.auth_service.controller;

import com.example.auth_service.model.BadInputParameters;
import com.example.auth_service.model.ConflictDataException;
import com.example.auth_service.dto.ErrorResponse;
import com.example.auth_service.model.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    void handleUserNotFoundException(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ConflictDataException.class)
    void handleConflictDataException(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(BadInputParameters.class)
    void handleBadInputParameters(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    void handleUsernameNotFoundException (
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    void handleRuntimeException(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    protected void sendResponse(HttpServletResponse response, int status, String errorMsg) throws IOException {
        response.setStatus(status);
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            bw.write(mapper.writeValueAsString(new ErrorResponse(errorMsg)));
        }

    }
}
