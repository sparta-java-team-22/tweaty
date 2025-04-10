package com.tweaty.payment.presentation.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.payment.application.dto.PaymentIdDto;
import com.tweaty.payment.application.service.PaymentService;
import com.tweaty.payment.global.SuccessResponse;
import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/{storeId}")
	public ResponseEntity<?> createPayment(@RequestBody PaymentRequestDto req, @PathVariable UUID storeId) {

		// TODO: 유저아이디 헤더 추가
		UUID userId = UUID.randomUUID();

		PaymentIdDto paymentIdDto = new PaymentIdDto(paymentService.createPayment(req, userId, storeId));

		return SuccessResponse.successWith(200, "결제 생성 성공", paymentIdDto);
	}
}
