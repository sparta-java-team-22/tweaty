package com.tweaty.payment.application.service;

import java.util.UUID;

import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;

public interface PaymentService {


	 UUID createPayment(PaymentRequestDto req,UUID userId,UUID storeId);
}
