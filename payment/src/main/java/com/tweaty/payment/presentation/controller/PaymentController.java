package com.tweaty.payment.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.payment.application.dto.PaymentIdDto;
import com.tweaty.payment.application.dto.RefundIdDto;
import com.tweaty.payment.application.service.PaymentService;
import com.tweaty.payment.global.SuccessResponse;
import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;
import com.tweaty.payment.presentation.dto.request.RefundRequestDto;
import com.tweaty.payment.presentation.dto.response.PaymentListResponse;
import com.tweaty.payment.presentation.dto.response.PaymentResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/{storeId}")
	public ResponseEntity<?> createPayment(@RequestBody PaymentRequestDto req, @PathVariable UUID storeId) {

		// TODO: 유저아이디 헤더 추가
		UUID userId = UUID.fromString("d263308c-5bf0-4362-9c51-a4c42c123456");

		PaymentIdDto paymentIdDto = new PaymentIdDto(paymentService.createPayment(req, userId, storeId));

		return SuccessResponse.successWith(200, "결제 생성 성공", paymentIdDto);
	}

	@GetMapping("/success")
	public ResponseEntity<?> getPaymentList(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		// TODO: 유저아이디 헤더 추가
		UUID userId = UUID.fromString("d263308c-5bf0-4362-9c51-a4c42c123456");

		Page<PaymentResponseDto> paymentPage = paymentService.getPayment(userId, page, size);

		return SuccessResponse.successWith(200, "결제 내역조회 성공", PaymentListResponse.from(paymentPage));
	}


	@PostMapping("/{paymentId}/refund")
	public ResponseEntity<?> createRefund(@RequestBody RefundRequestDto req, @PathVariable UUID paymentId) {

		// TODO: 유저아이디 헤더 추가
		UUID userId = UUID.fromString("d263308c-5bf0-4362-9c51-a4c42c123456");

		RefundIdDto refundIdDto = new RefundIdDto(paymentService.createRefund(req, userId, paymentId));

		return SuccessResponse.successWith(200, "환불 생성 성공", refundIdDto);
	}

}
