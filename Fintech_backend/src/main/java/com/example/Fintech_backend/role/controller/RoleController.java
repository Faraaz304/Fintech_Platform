package com.example.Fintech_backend.role.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Fintech_backend.res.Response;
import com.example.Fintech_backend.role.dto.CreateRoleRequest;
import com.example.Fintech_backend.role.dto.RoleDto;
import com.example.Fintech_backend.role.dto.UpdateRoleRequest;
import com.example.Fintech_backend.role.services.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<Response<RoleDto>> createRole(@Valid @RequestBody CreateRoleRequest request) {
        RoleDto roleDto = roleService.createRole(request);
        Response<RoleDto> response = Response.<RoleDto>builder()
                .status("success")
                .message("Role created successfully")
                .data(roleDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<RoleDto>> getRoleById(@PathVariable Long id) {
        RoleDto roleDto = roleService.getRoleById(id);
        Response<RoleDto> response = Response.<RoleDto>builder()
                .status("success")
                .message("Role retrieved successfully")
                .data(roleDto)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Response<RoleDto>> getRoleByName(@RequestParam String name) {
        RoleDto roleDto = roleService.getRoleByName(name);
        Response<RoleDto> response = Response.<RoleDto>builder()
                .status("success")
                .message("Role retrieved successfully")
                .data(roleDto)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response<List<RoleDto>>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        Response<List<RoleDto>> response = Response.<List<RoleDto>>builder()
                .status("success")
                .message("Roles retrieved successfully")
                .data(roles)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<RoleDto>> updateRole(@PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequest request) {
        RoleDto roleDto = roleService.updateRole(id, request);
        Response<RoleDto> response = Response.<RoleDto>builder()
                .status("success")
                .message("Role updated successfully")
                .data(roleDto)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        Response<Void> response = Response.<Void>builder()
                .status("success")
                .message("Role deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}