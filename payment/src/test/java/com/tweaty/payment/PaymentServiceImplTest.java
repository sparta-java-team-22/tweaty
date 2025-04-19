package com.tweaty.payment;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import com.tweaty.payment.application.service.PaymentService;
import com.tweaty.payment.domain.entity.MethodType;
import com.tweaty.payment.domain.entity.Payment;
import com.tweaty.payment.domain.repository.PaymentRepository;
import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;

@SpringBootTest
@ActiveProfiles("test")
	// @Transactional   <-- 이거 지워버려
class PaymentServiceImplTest {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PaymentRepository paymentRepository;

	@DisplayName("결제 생성 성공 테스트")
	@Test
	@Commit
		// <-- 요거 추가! (import org.springframework.test.annotation.Commit)
	void testCreatePayment_Success() {
		UUID storeId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();

		PaymentRequestDto requestDto = PaymentRequestDto.builder()
			.originalAmount(10000)
			.method(MethodType.CREDIT)
			.build();

		UUID paymentId = paymentService.createPayment(requestDto, userId, storeId);

		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new RuntimeException("결제가 존재하지 않습니다."));

		assertThat(payment.getId()).isEqualTo(paymentId);
		assertThat(payment.getStoreId()).isEqualTo(storeId);
		assertThat(payment.getOriginalAmount()).isEqualTo(10000);
		assertThat(payment.getStatus().name()).isEqualTo("SUCCESS");
		assertThat(payment.getMethod()).isEqualTo(MethodType.CREDIT);
	}
}