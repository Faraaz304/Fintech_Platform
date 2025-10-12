package com.example.Fintech_backend.auth_user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Fintech_backend.auth_user.entity.PasswordResetCode;

public interface PasswordResetCodeRepo extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findByCode(String code);
    void deleteByUserId(Long id);

}
