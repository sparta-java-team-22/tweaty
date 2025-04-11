package com.tweaty.payment.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.tweaty.payment.domain.entity.Refund;
import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;
import com.tweaty.payment.presentation.dto.request.RefundRequestDto;
import com.tweaty.payment.presentation.dto.response.PaymentResponseDto;

public interface PaymentService {

	UUID createPayment(PaymentRequestDto req, UUID userId, UUID storeId);

	Page<PaymentResponseDto> getPayment(UUID userId, int page, int size);


	UUID createRefund(RefundRequestDto req, UUID userId, UUID paymentId);

}
