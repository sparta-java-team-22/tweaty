package com.tweaty.reservation.presentation.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.tweaty.reservation.presentation.request.PaymentRequestDto;
import com.tweaty.reservation.presentation.request.RefundRequestDto;

@FeignClient(name = "payment-service")
public interface PaymentClient {

	@PostMapping("/{reservationId}/kafka")
	ResponseEntity<?> createKafkaPayment(
		@RequestHeader("X-USER-ID") UUID userId,
		@RequestBody PaymentRequestDto requestDto,
		@PathVariable("reservationId") UUID reservationId  // ← name 지정 필요
	);

	@PostMapping("/{paymentId}/refund/kafka")
	ResponseEntity<?> createRefund(
		@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role,
		@RequestBody RefundRequestDto requestDto,
		@PathVariable("paymentId") UUID paymentId);
}
