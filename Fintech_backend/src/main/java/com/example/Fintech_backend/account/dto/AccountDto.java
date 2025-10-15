package com.example.Fintech_backend.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.Fintech_backend.auth_user.dto.UserDto;
import com.example.Fintech_backend.enums.AccountStatus;
import com.example.Fintech_backend.enums.AccountType;
import com.example.Fintech_backend.enums.Currency;
import com.example.Fintech_backend.transaction.dto.TransactionDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class AccountDto {

    private Long id;
    private String accountNumber;
    private AccountType accountType;

    @JsonBackReference
    private UserDto user;

    private Currency currency;
    private BigDecimal balance;
    private AccountStatus Status;

    @JsonManagedReference
    private List<TransactionDto> transactions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;

}
