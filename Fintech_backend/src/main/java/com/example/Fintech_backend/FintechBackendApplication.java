package com.example.Fintech_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.Fintech_backend.auth_user.entity.User;
import com.example.Fintech_backend.enums.NotificationType;
import com.example.Fintech_backend.notification.dto.NotificationDto;
import com.example.Fintech_backend.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class FintechBackendApplication {

	private final NotificationService notificationService;

	public static void main(String[] args) {
		SpringApplication.run(FintechBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			log.info("Starting email test...");

			try {
				// Create a proper test user
				User testUser = User.builder()
						.id(1)
						.firstname("Test")
						.lastname("User")
						.email("mfaraazshaikh@gmail.com")
						.build();

				NotificationDto notificationDto = NotificationDto.builder()
						.recipient("mfaraazshaikh@gmail.com")
						.subject("Test Email from Fintech Backend")
						.body("<h1>Hello!</h1><p>This is a test email from your Fintech application.</p><p>If you receive this, your email service is working correctly!</p>")
						.type(NotificationType.EMAIL)
						.build();

				log.info("Sending test email to: {}", notificationDto.getRecipient());
				notificationService.sendEmail(notificationDto, testUser);
				log.info("Test email sent successfully!");

			} catch (Exception e) {
				log.error("Error sending test email: ", e);
			}
		};
	}
}
