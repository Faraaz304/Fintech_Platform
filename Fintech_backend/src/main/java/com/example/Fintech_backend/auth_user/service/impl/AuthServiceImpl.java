package com.example.Fintech_backend.auth_user.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Fintech_backend.account.entity.Account;
import com.example.Fintech_backend.account.repository.AccountRepo;
import com.example.Fintech_backend.auth_user.dto.LoginRequest;
import com.example.Fintech_backend.auth_user.dto.LoginResponse;
import com.example.Fintech_backend.auth_user.dto.PasswordResetRequest;
import com.example.Fintech_backend.auth_user.dto.RegistrationRequest;
import com.example.Fintech_backend.auth_user.entity.PasswordResetCode;
import com.example.Fintech_backend.auth_user.entity.User;
import com.example.Fintech_backend.auth_user.repository.PasswordResetCodeRepo;
import com.example.Fintech_backend.auth_user.repository.UserRepo;
import com.example.Fintech_backend.auth_user.service.AuthService;
import com.example.Fintech_backend.auth_user.service.CodeGenerator;
import com.example.Fintech_backend.enums.AccountStatus;
import com.example.Fintech_backend.enums.AccountType;

import com.example.Fintech_backend.exceptions.BadRequestException;
import com.example.Fintech_backend.exceptions.NotFoundException;
import com.example.Fintech_backend.notification.dto.NotificationDto;
import com.example.Fintech_backend.notification.service.NotificationService;
import com.example.Fintech_backend.res.Response;
import com.example.Fintech_backend.role.entity.Role;
import com.example.Fintech_backend.role.repository.RoleRepo;
import com.example.Fintech_backend.enums.Currency;
import com.example.Fintech_backend.security.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

        private final UserRepo userRepo;
        private final RoleRepo roleRepo;
        private final PasswordResetCodeRepo passwordResetCodeRepo;
        private final CodeGenerator codeGenerator;
        private final PasswordEncoder passwordEncoder;
        private final AuthService authService;
        private final TokenService tokenService;
        private final NotificationService notificationService;
        private final AccountRepo accountRepo;

        @Value("${app.password-reset.link}")
        private String passwordResetLink;

        @Override
        public Response<String> register(RegistrationRequest request) {
                log.info("Registering user with email: {}", request.getEmail());

                // Check if user already exists
                if (userRepo.findByEmail(request.getEmail()).isPresent()) {
                        throw new BadRequestException("User with this email already exists");
                }

                // Get roles or assign default USER role
                List<Role> roles;
                if (request.getRoles() == null || request.getRoles().isEmpty()) {
                        Role defaultRole = roleRepo.findByName("USER")
                                        .orElseThrow(() -> new NotFoundException("Default USER role not found"));
                        roles = Collections.singletonList(defaultRole);
                } else {
                        roles = request.getRoles().stream()
                                        .map(roleName -> roleRepo.findByName(roleName)
                                                        .orElseThrow(() -> new NotFoundException(
                                                                        "Role not found: " + roleName)))
                                        .collect(Collectors.toList());
                }

                // Create user
                User user = User.builder()
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .email(request.getEmail())
                                .phoneNumber(request.getPhoneNumber())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .roles(roles)
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build();

                User savedUser = userRepo.save(user);
                log.info("User registered successfully with ID: {}", savedUser.getId());

                // Create account for the user
                String accountNumber = generateAccountNumber();
                Account account = Account.builder()
                                .accountNumber(accountNumber)
                                .accountType(AccountType.SAVINGS)
                                .currency(Currency.USD)
                                .balance(java.math.BigDecimal.ZERO)
                                .accountStatus(AccountStatus.ACTIVE)
                                .user(savedUser)
                                .build();

                Account savedAccount = accountRepo.save(account);

                Map<String, Object> Vars = new HashMap<>();
                Vars.put("name", savedUser.getFirstName());

                // Send welcome email
                NotificationDto notificationDto = NotificationDto.builder()
                                .recipient(savedUser.getEmail())
                                .subject("Welcome to Fintech")
                                .body("Thank you for registering with us. Your account has been created successfully.")
                                .templateVariables(Vars)
                                .build();

                notificationService.sendEmail(notificationDto, savedUser);
                // send account creation details email

                Map<String, Object> accountVars = new HashMap<>();
                accountVars.put("accountNumber", savedAccount.getAccountNumber());
                accountVars.put("accountType", AccountType.SAVINGS.name());
                accountVars.put("name", savedUser.getFirstName());
                accountVars.put("currency", Currency.USD.name());

                NotificationDto accountCratedEmail = NotificationDto.builder()
                                .recipient(savedUser.getEmail())
                                .subject("your account has beeen created")
                                .body("accpount created")
                                .templateVariables(accountVars)
                                .build();

                notificationService.sendEmail(accountCratedEmail, savedUser);

                return Response.<String>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("account created successfully")
                                .data("Email of your account details has been sent to you. Your account number is: "
                                                + savedAccount.getAccountNumber())
                                .build();
        }

        @Override
        public Response<LoginResponse> login(LoginRequest loginRequest) {

                String email = loginRequest.getEmail();
                String password = loginRequest.getPassword();

                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
                if (!passwordEncoder.matches(password, user.getPassword())) {
                        throw new BadRequestException("Invalid password");
                }

                String token = tokenService.generateToken(user.getEmail());
                LoginResponse loginResponse = LoginResponse.builder()
                                .token(token)
                                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                                .build();

                return Response.<LoginResponse>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Login successful")
                                .data(loginResponse)
                                .build();

        }

        @Override
        @Transactional
        public Response<?> forgetPassword(String email) {
                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("User Not Found"));

                passwordResetCodeRepo.deleteByUserId(user.getId().longValue());

                String code = codeGenerator.generateUniqueCode();

                PasswordResetCode resetCode = PasswordResetCode.builder()
                                .user(user)
                                .code(code)
                                .expiryDate(calculateExpiryDate())
                                .used(false)
                                .build();

                passwordResetCodeRepo.save(resetCode);

                // send email reset link out
                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("name", user.getFirstName());
                templateVariables.put("resetLink", passwordResetLink + code);

                NotificationDto notificationDTO = NotificationDto.builder()
                                .recipient(user.getEmail())
                                .subject("Password Reset Code")
                                .templateName("password-reset")
                                .templateVariables(templateVariables)
                                .build();

                notificationService.sendEmail(notificationDTO, user);

                return Response.builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Password reset code sent to your email")
                                .build();
        }

        @Override
        @Transactional
        public Response<String> updatePasswordViaResetCode(PasswordResetRequest resetPasswordRequest) {
                log.info("Password reset attempt with code: {}", resetPasswordRequest.getCode());

                // Find reset code
                PasswordResetCode resetCode = passwordResetCodeRepo.findByCode(resetPasswordRequest.getCode())
                                .orElseThrow(() -> new BadRequestException("Invalid reset code"));

                // Check if code is expired
                if (resetCode.getExpiryDate().isBefore(LocalDateTime.now())) {
                        throw new BadRequestException("Reset code has expired");
                }

                // Check if code is already used
                if (resetCode.isUsed()) {
                        throw new BadRequestException("Reset code has already been used");
                }

                // Verify email matches
                if (!resetCode.getUser().getEmail().equals(resetPasswordRequest.getEmail())) {
                        throw new BadRequestException("Email does not match reset code");
                }

                // Update password
                User user = resetCode.getUser();
                user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
                user.setUpdatedAt(LocalDateTime.now());
                userRepo.save(user);

                // Mark reset code as used
                resetCode.setUsed(true);
                passwordResetCodeRepo.save(resetCode);

                // Send password reset confirmation email using HashMap pattern
                Map<String, Object> confirmationVars = new HashMap<>();
                confirmationVars.put("name", user.getFirstName());
                confirmationVars.put("resetTime", LocalDateTime.now().toString());

                NotificationDto confirmationNotification = NotificationDto.builder()
                                .recipient(user.getEmail())
                                .subject("Password Reset Successful")
                                .body("Your password has been successfully reset. If you did not make this change, please contact support immediately.")
                                .templateVariables(confirmationVars)
                                .build();

                notificationService.sendEmail(confirmationNotification, user);

                log.info("Password updated successfully for user: {}", user.getEmail());

                return Response.<String>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Password updated successfully")
                                .data("Password has been reset for user: " + user.getEmail())
                                .build();
        }

        private LocalDateTime calculateExpiryDate() {
                return LocalDateTime.now().plusHours(1); // 1 hour expiry
        }

        private String generateAccountNumber() {
                // Generate a 10-digit account number
                return "ACC" + System.currentTimeMillis() % 10000000L;
        }
}
