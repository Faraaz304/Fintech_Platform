package com.example.Fintech_backend.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String error) {
        super(error);
    }
    
}
