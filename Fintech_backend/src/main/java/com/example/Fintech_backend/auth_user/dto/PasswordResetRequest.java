package com.example.Fintech_backend.auth_user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class PasswordResetRequest {

    private String email;

    private String code;

    private String newPassword;
}
