package com.example.Fintech_backend.auth_user.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.example.Fintech_backend.enums.AccountStatus;
import com.example.Fintech_backend.enums.AccountType;
import com.example.Fintech_backend.enums.NotificationType;
import com.example.Fintech_backend.exceptions.BadRequestException;
import com.example.Fintech_backend.exceptions.NotFoundException;
import com.example.Fintech_backend.notification.dto.NotificationDto;
import com.example.Fintech_backend.notification.service.NotificationService;
import com.example.Fintech_backend.res.Response;
import com.example.Fintech_backend.role.entity.Role;
import com.example.Fintech_backend.role.repository.RoleRepo;
import com.example.Fintech_backend.enums.Currency;
import com.example.Fintech_backend.role.entity.Role;
import com.example.Fintech_backend.role.repository.RoleRepo;
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
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;
        private final TokenService tokenService;
        private final NotificationService notificationService;
        private final AccountRepo accountRepo;

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
                                .firstname(request.getFirstName())
                                .lastname(request.getLastName())
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
                Vars.put("name", savedUser.getFirstname());

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
                accountVars.put("name", savedUser.getFirstname());
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
        public Response<?> forgetPassword(String email) {
                log.info("Password reset requested for email: {}", email);

                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

                // Delete any existing reset codes for this user
                passwordResetCodeRepo.deleteByUserId(user.getId().longValue());

                // Generate reset code
                String resetCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                // Create password reset code entity
                PasswordResetCode passwordResetCode = PasswordResetCode.builder()
                                .code(resetCode)
                                .user(user)
                                .expiryDate(LocalDateTime.now().plusHours(1)) // 1 hour expiry
                                .used(false)
                                .build();

                passwordResetCodeRepo.save(passwordResetCode);

                // Send email notification
                try {
                        NotificationDto notificationDto = NotificationDto.builder()
                                        .recipient(email)
                                        .subject("Password Reset Code")
                                        .body(buildPasswordResetEmailBody(user.getFirstname(), resetCode))
                                        .type(NotificationType.EMAIL)
                                        .build();

                        notificationService.sendEmail(notificationDto, user);
                        log.info("Password reset email sent to: {}", email);
                } catch (Exception e) {
                        log.error("Failed to send password reset email to: {}", email, e);
                }

                return Response.builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Password reset code sent to your email")
                                .build();
        }

        @Override
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

                log.info("Password updated successfully for user: {}", user.getEmail());

                return Response.<String>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("Password updated successfully")
                                .data("Password has been reset for user: " + user.getEmail())
                                .build();
        }

        private String buildPasswordResetEmailBody(String firstName, String resetCode) {
                return String.format(
                                """
                                                <html>
                                                <body>
                                                    <h2>Password Reset Request</h2>
                                                    <p>Hello %s,</p>
                                                    <p>You have requested to reset your password. Please use the following code to reset your password:</p>
                                                    <h3 style="color: #007bff; font-size: 24px; letter-spacing: 2px;">%s</h3>
                                                    <p>This code will expire in 1 hour.</p>
                                                    <p>If you did not request this password reset, please ignore this email.</p>
                                                    <br>
                                                    <p>Best regards,<br>Fintech Team</p>
                                                </body>
                                                </html>
                                                """,
                                firstName != null ? firstName : "User", resetCode);
        }

        private String generateAccountNumber() {
                // Generate a 10-digit account number
                return "ACC" + System.currentTimeMillis() % 10000000L;
        }
}
