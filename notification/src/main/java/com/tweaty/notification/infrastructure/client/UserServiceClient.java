package com.tweaty.notification.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.tweaty.notification.application.dto.UserMailDto;

@FeignClient(name = "user-service")
public interface UserServiceClient {

	@GetMapping("/api/v1/users/internal/mail")
	UserMailDto getUserMail(@RequestParam("receiverId")UUID receiverId,
		@RequestHeader("internal-request") String header);
}
