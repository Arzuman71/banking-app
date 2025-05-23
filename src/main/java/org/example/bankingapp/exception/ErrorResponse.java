package org.example.bankingapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private String timestamp;

    public ErrorResponse(int status,  String message) {
        this(status, message, LocalDateTime.now().toString());
    }
}