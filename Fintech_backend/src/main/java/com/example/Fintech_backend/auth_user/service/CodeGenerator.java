package com.example.Fintech_backend.auth_user.service;

import org.springframework.stereotype.Service;

import com.example.Fintech_backend.auth_user.repository.PasswordResetCodeRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CodeGenerator {
    private final PasswordResetCodeRepo passwordResetCodeRepo;
    
    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 5;

    public String generateUniqueCode() {
        String code ;
        do{
            code = generateRandomCode();
        }while(passwordResetCodeRepo.findByCode(code).isPresent());
        return code;
    }
    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = (int) (Math.random() * ALPHA_NUMERIC.length());
            sb.append(ALPHA_NUMERIC.charAt(index));
        }
        return sb.toString();
    }
}
