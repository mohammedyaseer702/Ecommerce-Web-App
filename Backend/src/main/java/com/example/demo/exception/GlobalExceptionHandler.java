package com.example.demo.exception;

import com.example.demo.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;


import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 401
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Enum or parameter errors
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message("Invalid parameter value: " + ex.getName())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Parameter")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Fallback (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(
            Exception ex) {

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message("Something went wrong")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .details(List.of(ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException ex) {

    List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();

    ErrorResponse response = ErrorResponse.builder()
            .success(false)
            .message("Validation failed")
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Error")
            .details(errors)
            .timestamp(LocalDateTime.now())
            .build();

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}

}
