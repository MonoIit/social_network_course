package com.example.post_service.controller;

import com.example.post_service.dto.ErrorResponse;
import com.example.post_service.dto.PostNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    void handlePostNotFoundException(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
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
