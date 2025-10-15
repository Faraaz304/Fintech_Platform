package com.example.Fintech_backend.notification.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.example.Fintech_backend.auth_user.entity.User;
import com.example.Fintech_backend.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDto {

    private Long id;
    private String subject;
    @NotBlank(message = "Recipient is required")
    private String recipient;
    private String body;
    private NotificationType type;
    private User user;
    private LocalDateTime createdAt;
    private String templateName;
    private Map<String, Object> templateVariables;
}
