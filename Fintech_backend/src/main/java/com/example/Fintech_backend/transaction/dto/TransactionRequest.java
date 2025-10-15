package com.example.Fintech_backend.transaction.dto;

import java.math.BigDecimal;

import com.example.Fintech_backend.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {
    
    private TransactionType transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String description;
    private String destinationAccountNumber;
}
