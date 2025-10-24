package com.example.Fintech_backend.transaction.controller;

import com.example.Fintech_backend.res.Response;
import com.example.Fintech_backend.transaction.dto.TransactionDto;
import com.example.Fintech_backend.transaction.dto.TransactionRequest;
import com.example.Fintech_backend.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<Response<?>> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        Response<?> response = transactionService.createTransaction(transactionRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<Response<List<TransactionDto>>> getTransactionsByAccount(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Response<List<TransactionDto>> response = transactionService.getTransactionForAnAccount(accountNumber, page,
                size);
        return ResponseEntity.ok(response);
    }
}
