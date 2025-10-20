package com.example.Fintech_backend.transaction.service;

import java.util.List;

import com.example.Fintech_backend.res.Response;
import com.example.Fintech_backend.transaction.dto.TransactionDto;
import com.example.Fintech_backend.transaction.dto.TransactionRequest;

public interface TransactionService {
    Response<?> createTransaction(TransactionRequest transactionRequest);
    Response<List<TransactionDto>> getTransactionForAnAccount(String accountNumber , int page,int size);
}
