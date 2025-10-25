package com.example.Fintech_backend.audit_dashboard.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.Fintech_backend.account.dto.AccountDto;
import com.example.Fintech_backend.auth_user.dto.UserDto;
import com.example.Fintech_backend.transaction.dto.TransactionDto;

public interface Audit_Service {
    Map<String, Long> getSystemTotals();
    Optional<UserDto> findUserbyEmail(String email);
    Optional<AccountDto> findAccountDetailsByAccountNumber(String accountNumber);
    List<TransactionDto> findTransactionByAccountNumber(String accountNumber);
    Optional<TransactionDto> findTransactionById(Long transactionId);
      
    
} 