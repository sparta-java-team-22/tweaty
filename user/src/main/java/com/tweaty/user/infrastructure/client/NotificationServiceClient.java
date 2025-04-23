package com.tweaty.user.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tweaty.user.application.dto.NotificationRequestDto;

@FeignClient(name = "notification-service")
public interface NotificationServiceClient {

	@PostMapping("/api/v1/notifications/signup")
	void createSignupNotification(@RequestBody NotificationRequestDto requestDto);

}
