package com.example.Fintech_backend.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.Fintech_backend.account.entity.Account;
import com.example.Fintech_backend.enums.TransactionStatus;
import com.example.Fintech_backend.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {

    private Long id;

    private BigDecimal amount;

    private TransactionType transactionType;

    private LocalDateTime transactionDate;

    private String description;

    private TransactionStatus transactionStatus;

    @JsonBackReference
    private Account account;

    private String sourceAccount;
    private String destinationAccount;
}
