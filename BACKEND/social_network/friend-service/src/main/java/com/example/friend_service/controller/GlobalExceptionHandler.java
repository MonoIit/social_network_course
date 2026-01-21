package com.example.friend_service.controller;

import com.example.friend_service.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
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

    @ExceptionHandler(RuntimeException.class)
    void handleRuntimeException(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(FriendshipAlreadyExistsException.class)
    void handleFriendshipAlreadyExistsException(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(FriendshipIsBlockedException.class)
    void handleFriendshipIsBlockedException(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler(FriendshipNotFoundException.class)
    void handleFriendshipNotFoundException(
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        sendResponse(response, HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
    }

    protected void sendResponse(HttpServletResponse response, int status, String errorMsg) throws IOException {
        response.setStatus(status);
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            bw.write(mapper.writeValueAsString(new ErrorResponse(errorMsg)));
        }

    }
}
