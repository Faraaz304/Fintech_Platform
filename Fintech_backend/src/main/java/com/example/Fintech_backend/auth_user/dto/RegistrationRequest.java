package com.example.Fintech_backend.auth_user.dto;

import java.util.List;

import com.example.Fintech_backend.role.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class RegistrationRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @NotBlank(message = "Email is required")
    @Email
    private String email;
    private List<String>  roles;
    @NotBlank(message = "Password is required")
    private String password;
   
}
