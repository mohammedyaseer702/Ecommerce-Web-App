package com.example.demo.payload;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ErrorResponse {

    private boolean success;
    private String message;
    private int status;
    private String error;
    private List<String> details;
    private LocalDateTime timestamp;
}
