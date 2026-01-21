package com.example.user_service.controller;

import com.example.user_service.model.BadRequestParamException;
import com.example.user_service.model.ErrorResponse;
import com.example.user_service.model.ProfileNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProfileNotFoundException.class)
    void handleUserNotFoundException(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(BadRequestParamException.class)
    void handleConflictDataException(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
    }



    protected void sendResponse(HttpServletResponse response, int status, String errorMsg) throws IOException {
        response.setStatus(status);
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            bw.write(mapper.writeValueAsString(new ErrorResponse(errorMsg)));
        }

    }
}