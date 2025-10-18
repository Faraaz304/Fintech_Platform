package com.example.Fintech_backend.role.services;

import java.util.List;

import com.example.Fintech_backend.res.Response;
import com.example.Fintech_backend.role.entity.Role;

public interface RoleService {
    Response<Role> createRole(Role roleRequest);
    Response<Role> updateRole(Role roleRequest);
    Response<List<Role>> getAllRole();
    Response<?> deleteRole(Long id);

}
