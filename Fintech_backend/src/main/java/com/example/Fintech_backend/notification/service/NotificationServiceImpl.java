package com.example.Fintech_backend.notification.service;

import java.nio.charset.StandardCharsets;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.Fintech_backend.auth_user.entity.User;
import com.example.Fintech_backend.enums.NotificationType;
import com.example.Fintech_backend.notification.dto.NotificationDto;
import com.example.Fintech_backend.notification.entity.Notification;
import com.example.Fintech_backend.notification.repository.NotificationRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void sendEmail(NotificationDto NotificationDto, User user) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setTo(NotificationDto.getRecipient());
            helper.setSubject(NotificationDto.getSubject());
            if (NotificationDto.getTemplateName() != null) {
                Context context = new Context();
                context.setVariables(NotificationDto.getTemplateVariables());
                String html = templateEngine.process(NotificationDto.getTemplateName(), context);
                helper.setText(html, true);
            } else {
                helper.setText(NotificationDto.getBody(), true);
            }
            mailSender.send(mimeMessage);
            // Notification notificationToSave = Notification.builder()
            //         .subject(NotificationDto.getSubject())
            //         .recipient(NotificationDto.getRecipient())
            //         .body(NotificationDto.getBody())
            //         .type(NotificationType.EMAIL)
            //         .user(user)
            //         .build();

            // notificationRepo.save(notificationToSave);

        } catch (MessagingException e) {
            log.error("Error sending email", e);
        }

    }

}
