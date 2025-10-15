package com.example.Fintech_backend.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String error) {
        super(error);
    }
}
