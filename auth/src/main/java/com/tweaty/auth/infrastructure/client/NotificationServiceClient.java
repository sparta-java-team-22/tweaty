package com.tweaty.auth.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tweaty.auth.application.dto.NotificationRequestDto;

@FeignClient(name = "notification-service")
public interface NotificationServiceClient {

	@PostMapping("/api/v1/notifications/signup")
	void createSignupNotification(@RequestBody NotificationRequestDto requestDto);

}
