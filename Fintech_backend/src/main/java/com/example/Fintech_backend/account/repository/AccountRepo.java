package com.example.Fintech_backend.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Fintech_backend.account.entity.Account;

public interface AccountRepo extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    List<Account> findByUserId(Long userId);
}
