package com.example.Fintech_backend.account.service;

import java.util.List;

import com.example.Fintech_backend.account.dto.AccountDto;
import com.example.Fintech_backend.account.entity.Account;
import com.example.Fintech_backend.auth_user.entity.User;
import com.example.Fintech_backend.enums.AccountType;
import com.example.Fintech_backend.res.Response;

public interface AccountService {
    Account createAccount(AccountType accountType, User user);
    Response<List<AccountDto>> getMyAccounts();
    Response<?> closeAccount(String accountNumber);
}
