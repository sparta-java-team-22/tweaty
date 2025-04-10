package com.tweaty.payment.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;
import com.tweaty.payment.presentation.dto.response.PaymentResponseDto;

public interface PaymentService {

	UUID createPayment(PaymentRequestDto req, UUID userId, UUID storeId);

	Page<PaymentResponseDto> getPayment(UUID userId, int page, int size);

}
