package com.example.messenger_service.controllers;

import com.example.messenger_service.model.ErrorResponse;
import com.example.messenger_service.model.NotEnoughPrivileges;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotEnoughPrivileges.class)
    void handleNotEnoughPrivileges(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_FORBIDDEN, exception.getMessage());
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
