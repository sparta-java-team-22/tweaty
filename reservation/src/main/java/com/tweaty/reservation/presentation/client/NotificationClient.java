package com.tweaty.reservation.presentation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tweaty.reservation.presentation.request.NotificationRequestDto;

@FeignClient(name = "notification-service")
public interface NotificationClient {

	@PostMapping("/reservation")
	ResponseEntity<Void> createReservationNotification(@RequestBody NotificationRequestDto requestDto);
}
