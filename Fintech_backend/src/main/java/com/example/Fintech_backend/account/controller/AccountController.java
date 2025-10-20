package com.example.Fintech_backend.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Fintech_backend.account.service.AccountService;
import com.example.Fintech_backend.res.Response;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    
    @GetMapping("/me")
    public ResponseEntity<Response<?>> getMyaccounts() {
        return ResponseEntity.ok(accountService.getMyAccounts());
    }

    @GetMapping("/close/{accountnumber}")
    public ResponseEntity<Response<?>> closeAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.closeAccount(accountNumber));
    }
    
    
}
