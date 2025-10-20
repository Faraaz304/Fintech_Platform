package com.example.Fintech_backend.auth_user.service.impl;

import com.example.Fintech_backend.auth_user.dto.UpdatePasswordRequest;
import com.example.Fintech_backend.auth_user.dto.UserDto;
import com.example.Fintech_backend.auth_user.entity.User;
import com.example.Fintech_backend.auth_user.repository.UserRepo;
import com.example.Fintech_backend.auth_user.service.UserService;
import com.example.Fintech_backend.exceptions.BadRequestException;
import com.example.Fintech_backend.exceptions.NotFoundException;
import com.example.Fintech_backend.notification.dto.NotificationDto;
import com.example.Fintech_backend.notification.service.NotificationService;
import com.example.Fintech_backend.res.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    // Directory for storing profile pictures
    private final String uploadDir = "uploads/profile-pictures/";

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new NotFoundException("User is not authenticated");
        }
        String email = authentication.getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Response<UserDto> getMyProfile() {
        User user = getCurrentLoggedInUser();
        UserDto userDto = modelMapper.map(user, UserDto.class);

        return Response.<UserDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User profile retrieved successfully")
                .data(userDto)
                .build();
    }

    @Override
    public Response<Page<UserDto>> getAllUsers(int page, int size) {
        Page<User> users = userRepo.findAll(PageRequest.of(page, size));
        Page<UserDto> userDtos = users.map(user -> modelMapper.map(user, UserDto.class));

        return Response.<Page<UserDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Users retrieved successfully")
                .data(userDtos)
                .build();
    }

    @Override
    public Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = getCurrentLoggedInUser();
        String newPassword = updatePasswordRequest.getNewPassword();
        String oldPassword = updatePasswordRequest.getOldPassword();

        if (oldPassword == null || newPassword == null) {
            throw new BadRequestException("Old and New Password Required");
        }

        // Validate the old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old Password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);

        // Send password change confirmation email
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", user.getFirstName());

        NotificationDto notificationDto = NotificationDto.builder()
                .recipient(user.getEmail())
                .subject("Your Password Was Successfully Changed")
                .templateName("password-change")
                .templateVariables(templateVariables)
                .build();

        notificationService.sendEmail(notificationDto, user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password changed successfully")
                .build();
    }

    @Override
    public Response<?> uploadProfilePicture(MultipartFile file) {
        User user = getCurrentLoggedInUser();

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Delete old profile picture if exists
            if (user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()) {
                Path oldFile = Paths.get(user.getProfilePictureUrl());
                if (Files.exists(oldFile)) {
                    Files.delete(oldFile);
                }
            }

            // Generate a unique file name to avoid conflicts
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID() + fileExtension;
            Path filePath = uploadPath.resolve(newFileName);

            // Save the file
            Files.copy(file.getInputStream(), filePath);

            String fileUrl = uploadDir + newFileName;

            // Update user profile picture URL
            user.setProfilePictureUrl(fileUrl);
            user.setUpdatedAt(LocalDateTime.now());
            userRepo.save(user);

            return Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Profile picture uploaded successfully")
                    .data(fileUrl)
                    .build();

        } catch (IOException e) {
            log.error("Error uploading profile picture: {}", e.getMessage());
            throw new RuntimeException("Failed to upload profile picture: " + e.getMessage());
        }
    }
}
