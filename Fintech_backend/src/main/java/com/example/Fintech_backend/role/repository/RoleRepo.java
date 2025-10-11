package com.example.Fintech_backend.role.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Fintech_backend.role.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

}
