package com.example.Fintech_backend.exceptions;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(String error) {
        super(error);
    }
}
