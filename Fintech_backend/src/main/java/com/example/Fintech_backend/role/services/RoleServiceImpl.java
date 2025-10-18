package com.example.Fintech_backend.role.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Fintech_backend.exceptions.BadRequestException;
import com.example.Fintech_backend.exceptions.NotFoundException;
import com.example.Fintech_backend.res.Response;
import com.example.Fintech_backend.role.entity.Role;
import com.example.Fintech_backend.role.repository.RoleRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    @Override
    public Response<Role> createRole(Role roleRequest) {
        if (roleRepo.findByName(roleRequest.getName()).isPresent()) {
            throw new BadRequestException("Role already exists");
        }

        Role savedRole = roleRepo.save(roleRequest);
        return Response.<Role>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Role created successfully")
                .data(savedRole)
                .build();
    }

    @Override
    public Response<Role> updateRole(Role roleRequest) {
        if (roleRequest.getId() == null) {
            throw new BadRequestException("Role ID is required");
        }

        if (!roleRepo.existsById(roleRequest.getId())) {
            throw new NotFoundException("Role not found");
        }

        Role updatedRole = roleRepo.save(roleRequest);
        return Response.<Role>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role updated successfully")
                .data(updatedRole)
                .build();
    }

    @Override
    public Response<List<Role>> getAllRole() {
        List<Role> roles = roleRepo.findAll();
        return Response.<List<Role>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Roles retrieved successfully")
                .data(roles)
                .build();
    }

    @Override
    public Response<?> deleteRole(Long id) {
        if (!roleRepo.existsById(id)) {
            throw new NotFoundException("Role not found");
        }

        roleRepo.deleteById(id);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role deleted successfully")
                .build();
    }
}