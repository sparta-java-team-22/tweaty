package com.tweaty.payment.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.PaymentType;
import com.tweaty.payment.domain.entity.Refund;
import com.tweaty.payment.domain.repository.PaymentRepository;
import com.tweaty.payment.domain.service.PaymentDomainService;
import com.tweaty.payment.global.SuccessResponse;
import com.tweaty.payment.global.exception.CustomException;
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

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;
	private final KafkaPaymentProducer kafkaPaymentProducer;
	private final KafkaRefundProducer kafkaRefundProducer;
	private final PaymentDomainService paymentDomainService;

	@PostMapping("/{reservationId}")
	public ResponseEntity<?> createPayment(@RequestBody PaymentRequestDto req, @PathVariable UUID reservationId) {

		// TODO: 유저아이디 헤더 추가
		UUID userId = UUID.fromString("d263308c-5bf0-4362-9c51-a4c42c123456");

		// 결제 객체 생성(결제요청 상태)
		Payment payment = Payment.toReadyEntity(req, reservationId, userId);
		paymentDomainService.saveReadyPayment(payment);

		PaymentIdDto paymentIdDto = new PaymentIdDto(paymentService.createPayment(payment));

		return SuccessResponse.successWith(200, "결제 생성 성공", paymentIdDto);
	}

	@PostMapping("/{reservationId}/kafka")
	public ResponseEntity<?> createKafkaPayment(@RequestBody PaymentRequestDto req, @PathVariable UUID reservationId) {

		// TODO: 유저아이디 헤더 추가
		UUID userId = UUID.fromString("d263308c-5bf0-4362-9c51-a4c42c123456");

		// 결제 객체 생성(결제요청 상태)
		Payment payment = Payment.toReadyEntity(req, reservationId, userId);
		paymentDomainService.saveReadyPayment(payment);

		kafkaPaymentProducer.sendCreateEvent(PaymentCreateEvent.from(payment));

		PaymentIdDto paymentIdDto = new PaymentIdDto(payment.getId());
		return SuccessResponse.successWith(200, "결제 생성 성공(kafka)", paymentIdDto);
	}

	@GetMapping
	public ResponseEntity<?> getPaymentList(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		// TODO: 유저아이디 헤더 추가
		UUID userId = UUID.fromString("d263308c-5bf0-4362-9c51-a4c42c123456");

		Page<PaymentResponseDto> paymentPage = paymentService.getPayment(userId, page, size);

		return SuccessResponse.successWith(200, "결제 내역조회 성공", PaymentListResponse.from(paymentPage));
	}

	@PostMapping("/{paymentId}/refund")
	public ResponseEntity<?> createKafkaRefund(@RequestBody RefundRequestDto req, @PathVariable UUID paymentId) {

		// TODO: 유저아이디 헤더 추가
		UUID userId = UUID.fromString("d263308c-5bf0-4362-9c51-a4c42c123456");

		RefundIdDto refundIdDto = new RefundIdDto(paymentService.createRefund(req, userId, paymentId));

		return SuccessResponse.successWith(200, "환불 생성 성공", refundIdDto);
	}

	@PostMapping("/{paymentId}/refund/kafka")
	public ResponseEntity<?> createRefund(@RequestBody RefundRequestDto req, @PathVariable UUID paymentId) {

		// TODO: 유저아이디 헤더 추가
		UUID userId = UUID.fromString("d263308c-5bf0-4362-9c51-a4c42c123456");

		Payment payment = paymentDomainService.findPayment(paymentId);

		Refund refund = Refund.toReadyEntity(req, userId, payment);
		paymentDomainService.saveReadyRefund(refund);

		kafkaRefundProducer.sendRefundEvent(RefundCreateEvent.from(refund));

		RefundIdDto refundIdDto = new RefundIdDto(refund.getId());

		return SuccessResponse.successWith(200, "환불 생성 성공", refundIdDto);
	}

	@GetMapping("/refund")
	public ResponseEntity<?> getRefundList(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		// TODO: 유저아이디 헤더 추가
		UUID userId = UUID.fromString("d263308c-5bf0-4362-9c51-a4c42c123456");

		Page<RefundResponseDto> refundPage = paymentService.getRefund(userId, page, size);

		return SuccessResponse.successWith(200, "환불 내역조회 성공", RefundListResponse.from(refundPage));
	}

}
