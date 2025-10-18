package com.example.Fintech_backend.auth_user.service;

import com.example.Fintech_backend.auth_user.dto.LoginRequest;
import com.example.Fintech_backend.auth_user.dto.LoginResponse;
import com.example.Fintech_backend.auth_user.dto.PasswordResetRequest;
import com.example.Fintech_backend.auth_user.dto.RegistrationRequest;
import com.example.Fintech_backend.res.Response;

public interface AuthService {
    Response<String> register(RegistrationRequest request);
    Response<LoginResponse> login(LoginRequest loginRequest);
    Response<?> forgetPassword(String email);
    Response<String> updatePasswordViaResetCode(PasswordResetRequest resetPasswordRequest);
}