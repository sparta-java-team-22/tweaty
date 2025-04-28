package com.tweaty.payment.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.payment.application.dto.PaymentIdDto;
import com.tweaty.payment.application.dto.RefundIdDto;
import com.tweaty.payment.application.service.PaymentService;
import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.Refund;
import com.tweaty.payment.domain.service.PaymentDomainService;

import com.tweaty.payment.infrastucture.kafka.event.PaymentCreateEvent;
import com.tweaty.payment.infrastucture.kafka.event.RefundCreateEvent;
import com.tweaty.payment.infrastucture.kafka.producer.KafkaPaymentProducer;
import com.tweaty.payment.infrastucture.kafka.producer.KafkaRefundProducer;
import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;
import com.tweaty.payment.presentation.dto.request.RefundRequestDto;
import com.tweaty.payment.presentation.dto.response.PaymentListResponse;
import com.tweaty.payment.presentation.dto.response.PaymentResponseDto;
import com.tweaty.payment.presentation.dto.response.RefundListResponse;
import com.tweaty.payment.presentation.dto.response.RefundResponseDto;

import lombok.RequiredArgsConstructor;
import response.SuccessResponse;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;
	private final KafkaPaymentProducer kafkaPaymentProducer;
	private final KafkaRefundProducer kafkaRefundProducer;
	private final PaymentDomainService paymentDomainService;

	@PostMapping("/{reservationId}/synchronize")
	public ResponseEntity<?> createPayment(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @RequestBody PaymentRequestDto req,
		@PathVariable UUID reservationId) {
		Payment payment = Payment.toReadyEntity(req, reservationId, userId);
		paymentDomainService.saveReadyPayment(payment);

		PaymentIdDto paymentIdDto = new PaymentIdDto(paymentService.createPayment(payment));

		return SuccessResponse.successWith(200, "결제 생성 성공", paymentIdDto);
	}

	// 비관적락 적용
	@PostMapping("/{reservationId}")
	public ResponseEntity<?> createKafkaPayment(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @RequestBody PaymentRequestDto req,
		@PathVariable UUID reservationId) {
		Payment payment = Payment.toReadyEntity(req, reservationId, userId);
		paymentDomainService.saveReadyPayment(payment);
		kafkaPaymentProducer.sendCreateEvent(PaymentCreateEvent.from(payment));

		PaymentIdDto paymentIdDto = new PaymentIdDto(payment.getId());

		return ResponseEntity.ok(paymentIdDto);
	}

	// // REDIS + 분산락적용
	@PostMapping("/{reservationId}/redis")
	public ResponseEntity<?> createKafkaPaymentV2(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @RequestBody PaymentRequestDto req,
		@PathVariable UUID reservationId) {

		Payment payment = paymentDomainService.toReadyPayment(req, userId, reservationId);
		kafkaPaymentProducer.sendCreateEvenByRedisson(PaymentCreateEvent.from(payment));
		PaymentIdDto paymentIdDto = new PaymentIdDto(payment.getId());

		return ResponseEntity.ok(paymentIdDto);
	}

	@GetMapping
	public ResponseEntity<?> getPaymentList(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		Page<PaymentResponseDto> paymentPage = paymentService.getPayment(userId, page, size);

		return SuccessResponse.successWith(200, "결제 내역조회 성공", PaymentListResponse.from(paymentPage));
	}

	@PostMapping("/{paymentId}/refund/synchronize")
	public ResponseEntity<?> createKafkaRefund(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @RequestBody RefundRequestDto req, @PathVariable UUID paymentId) {

		RefundIdDto refundIdDto = new RefundIdDto(paymentService.createRefund(req, userId, paymentId));

		return SuccessResponse.successWith(200, "환불 생성 성공", refundIdDto);
	}

	@PostMapping("/{paymentId}/refund")
	public ResponseEntity<?> createRefund(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @RequestBody RefundRequestDto req, @PathVariable UUID paymentId) {

		Payment payment = paymentDomainService.findPayment(paymentId);

		Refund refund = Refund.toReadyEntity(req, userId, payment);
		paymentDomainService.saveReadyRefund(refund);

		kafkaRefundProducer.sendRefundEvent(RefundCreateEvent.from(refund));

		return ResponseEntity.ok("환불 요청이 접수되었습니다.");
	}

	@GetMapping("/refund")
	public ResponseEntity<?> getRefundList(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		Page<RefundResponseDto> refundPage = paymentService.getRefund(userId, page, size);

		return SuccessResponse.successWith(200, "환불 내역조회 성공", RefundListResponse.from(refundPage));
	}

}
