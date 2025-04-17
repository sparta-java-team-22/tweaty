package com.tweaty.payment.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.entity.Refund;
import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;
import com.tweaty.payment.presentation.dto.request.RefundRequestDto;
import com.tweaty.payment.presentation.dto.response.PaymentResponseDto;
import com.tweaty.payment.presentation.dto.response.RefundResponseDto;

public interface PaymentService {

	UUID createPayment(Payment payment);

	Page<PaymentResponseDto> getPayment(UUID userId, int page, int size);


	UUID createRefund(RefundRequestDto req, UUID userId, UUID paymentId);

	Page<RefundResponseDto> getRefund(UUID userId, int page, int size);


	}
