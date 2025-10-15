package com.example.Fintech_backend.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String error) {
        super(error);
    }
    
}
