package com.example.Fintech_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class FintechBackendApplication {


	public static void main(String[] args) {
		SpringApplication.run(FintechBackendApplication.class, args);
	}

}
