package com.example.Fintech_backend.auth_user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Fintech_backend.auth_user.dto.LoginRequest;
import com.example.Fintech_backend.auth_user.dto.LoginResponse;
import com.example.Fintech_backend.auth_user.dto.PasswordResetRequest;
import com.example.Fintech_backend.auth_user.dto.RegistrationRequest;
import com.example.Fintech_backend.auth_user.service.AuthService;
import com.example.Fintech_backend.res.Response;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<String>> register(@Valid @RequestBody RegistrationRequest request) {
        log.info("Registration request received for email: {}", request.getEmail());
        Response<String> response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request received for email: {}", loginRequest.getEmail());
        Response<LoginResponse> response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Response<?>> forgotPassword(@RequestParam String email) {
        log.info("Forgot password request received for email: {}", email);
        Response<?> response = authService.forgetPassword(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Response<String>> resetPassword(@Valid @RequestBody PasswordResetRequest resetRequest) {
        log.info("Password reset request received for email: {}", resetRequest.getEmail());
        Response<String> response = authService.updatePasswordViaResetCode(resetRequest);
        return ResponseEntity.ok(response);
    }
}