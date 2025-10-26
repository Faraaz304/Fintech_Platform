// made litte change in this file 

package com.example.Fintech_backend.audit_dashboard.controller;

import com.example.Fintech_backend.audit_dashboard.service.Audit_Service;
import com.example.Fintech_backend.account.dto.AccountDto;
import com.example.Fintech_backend.auth_user.dto.UserDto;
import com.example.Fintech_backend.transaction.dto.TransactionDto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/audit")
@PreAuthorize("hasAnyAuthority('ADMIN', 'AUDITOR')")
@RequiredArgsConstructor
public class AuditController {

    private final Audit_Service auditService;

    // ✅ Get system totals: users, accounts, transactions
    @GetMapping("/totals")
    public ResponseEntity<Map<String, Long>> getSystemTotals() {
        return ResponseEntity.ok(auditService.getSystemTotals());
    }

    // ✅ Find user by email
    @GetMapping("/user")
    public ResponseEntity<?> findUserByEmail(@RequestParam String email) {
        Optional<UserDto> user = auditService.findUserbyEmail(email);
        return user.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Find account details by account number
    @GetMapping("/account")
    public ResponseEntity<?> findAccountByNumber(@RequestParam String accountNumber) {
        Optional<AccountDto> account = auditService.findAccountDetailsByAccountNumber(accountNumber);
        return account.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Find all transactions by account number
    @GetMapping("/transactions/by-account")
    public ResponseEntity<List<TransactionDto>> findTransactionsByAccount(@RequestParam String accountNumber) {
        List<TransactionDto> transactions = auditService.findTransactionByAccountNumber(accountNumber);
        return ResponseEntity.ok(transactions);
    }

    // ✅ Find transaction by ID
    @GetMapping("/transaction/by-id")
    public ResponseEntity<?> findTransactionById(@RequestParam Long id) {
        Optional<TransactionDto> transaction = auditService.findTransactionById(id);
        return transaction.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }
}
