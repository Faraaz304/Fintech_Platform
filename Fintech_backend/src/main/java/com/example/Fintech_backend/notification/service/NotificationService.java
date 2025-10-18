package com.example.Fintech_backend.notification.service;

import com.example.Fintech_backend.auth_user.entity.User;
import com.example.Fintech_backend.notification.dto.NotificationDto;

public interface NotificationService {

    void sendEmail(NotificationDto NotificationDto,User user);
} 
