package com.example.Fintech_backend.account.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Fintech_backend.account.dto.AccountDto;
import com.example.Fintech_backend.account.entity.Account;
import com.example.Fintech_backend.account.repository.AccountRepo;
import com.example.Fintech_backend.auth_user.entity.User;
import com.example.Fintech_backend.auth_user.service.UserService;
import com.example.Fintech_backend.enums.AccountStatus;
import com.example.Fintech_backend.enums.AccountType;
import com.example.Fintech_backend.enums.Currency;
import com.example.Fintech_backend.exceptions.BadRequestException;
import com.example.Fintech_backend.exceptions.NotFoundException;
import com.example.Fintech_backend.res.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    private final Random random = new Random();

    @Override
    public Account createAccount(AccountType accountType, User user) {
        log.info("Inside createAccount()");
        String accountNumber = generateAccountNumber();
        
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(accountType)
                .currency(Currency.USD)
                .balance(BigDecimal.ZERO)
                .accountStatus(AccountStatus.ACTIVE)
                .user(user)
                .build();
        
        return accountRepository.save(account);
    }

    @Override
    public Response<List<AccountDto>> getMyAccounts() {
        User user = userService.getCurrentLoggedInUser();
        List<AccountDto> accounts = accountRepository.findByUserId(user.getId())
                .stream()
                .map(account -> modelMapper.map(account, AccountDto.class))
                .toList();
        
        return Response.<List<AccountDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User accounts fetched successfully")
                .data(accounts)
                .build();
    }

    @Override
    public Response<?> closeAccount(String accountNumber) {
        User user = userService.getCurrentLoggedInUser();
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account Not Found"));
        
        // Check if account belongs to user by comparing user IDs
        if (!account.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Account doesn't belong to you");
        }
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestException("Account balance must be zero before closing");
        }
        
        account.setAccountStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
        
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account closed successfully")
                .build();
    }

    
    private String generateAccountNumber() {
        String accountNumber;
        do {
            // Generate a random 8-digit number (from 10,000,000 to 99,999,999)
            // and combine it with the "66" prefix.
            accountNumber = "66" + (random.nextInt(90000000) + 10000000);
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());
        
        log.info("account number generated {}", accountNumber);
        return accountNumber;
    }


}
