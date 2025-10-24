package com.example.Fintech_backend.audit_dashboard.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.example.Fintech_backend.account.dto.AccountDto;
import com.example.Fintech_backend.account.repository.AccountRepo;
import com.example.Fintech_backend.auth_user.dto.UserDto;
import com.example.Fintech_backend.auth_user.repository.UserRepo;
import com.example.Fintech_backend.transaction.dto.TransactionDto;
import com.example.Fintech_backend.transaction.entity.Transaction;
import com.example.Fintech_backend.transaction.repository.TransactionRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuditServiceImpl implements Audit_Service {
    
    private final UserRepo userRepo;
    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;
    private final ModelMapper modelMapper;
    
    @Override
    public Map<String, Long> getSystemTotals() {

        long totalUsers = userRepo.count();
        long totalAccounts = accountRepo.count();
        long totalTransactions = transactionRepo.count();

        return Map.of("totalUsers", totalUsers, "totalAccounts", totalAccounts, "totalTransactions", totalTransactions);

    }
    @Override
    public Optional<UserDto> findUserbyEmail(String email) {
        return userRepo.findByEmail(email).map(user -> modelMapper.map(user, UserDto.class));
    }
    @Override
    public Optional<AccountDto> findAccountDetailsByAccountNumber(String accountNumber) {
        return accountRepo.findByAccountNumber(accountNumber).map(account -> modelMapper.map(account, AccountDto.class));
    }
    @Override
    public List<TransactionDto> findTransactionByAccountNumber(String accountNumber) {
        return transactionRepo.findByAccount_AccountNumber(accountNumber).stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .toList();
    }
    @Override
    public Optional<TransactionDto> findTransactionById(Long transactionId) {
        return transactionRepo.findById(transactionId)
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class));
    }
}
