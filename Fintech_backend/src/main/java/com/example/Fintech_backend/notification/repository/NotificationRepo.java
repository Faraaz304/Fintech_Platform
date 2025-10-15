package com.example.Fintech_backend.notification.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Fintech_backend.notification.entity.Notification;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
    
}
