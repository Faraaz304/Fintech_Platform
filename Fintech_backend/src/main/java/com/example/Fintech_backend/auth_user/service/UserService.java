package com.example.Fintech_backend.auth_user.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.Fintech_backend.auth_user.dto.UpdatePasswordRequest;
import com.example.Fintech_backend.auth_user.dto.UserDto;
import com.example.Fintech_backend.auth_user.entity.User;
import com.example.Fintech_backend.res.Response;

public interface UserService {
    User getCurrentLoggedInUser();

    Response<UserDto> getMyProfile();

    Response<Page<UserDto>> getAllUsers(int page, int size);

    Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest);

    Response<?> uploadProfilePicture(MultipartFile file);
}
