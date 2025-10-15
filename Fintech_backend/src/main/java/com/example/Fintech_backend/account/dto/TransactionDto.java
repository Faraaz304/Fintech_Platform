package com.example.Fintech_backend.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.Fintech_backend.enums.TransactionStatus;
import com.example.Fintech_backend.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private Long id;
    private String transactionReference;
    private TransactionType transactionType;
    private BigDecimal amount;
    private Currency currency;
    private TransactionStatus status;
    
    @JsonBackReference
    private AccountDto account;
    
    private String description;
    private LocalDateTime createdAt;
}
