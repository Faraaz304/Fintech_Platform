package com.example.Fintech_backend.role.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Fintech_backend.res.Response;
import com.example.Fintech_backend.role.entity.Role;
import com.example.Fintech_backend.role.services.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {

    private RoleService roleService;

    @PostMapping
    public Response<Role> createRole(@Valid @RequestBody Role role) {
        return roleService.createRole(role);
    }

    @PutMapping
    public Response<Role> updateRole(@Valid @RequestBody Role role) {
        return roleService.updateRole(role);
    }

    @GetMapping
    public Response<List<Role>> getAllRoles() {
        return roleService.getAllRole();
    }

    @DeleteMapping("/{id}")
    public Response<?> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }
}