package com.tweaty.promotion.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.tweaty.promotion.application.dto.PromotionCreateResponse;
import com.tweaty.promotion.presentation.request.PromotionCreateRequest;

@SpringBootTest
@ActiveProfiles("test")
class PromotionServiceTest {
	@Autowired
	private PromotionService promotionService;

	@Test
	public void createPromotionTest() {
		// given
		PromotionCreateRequest request = new PromotionCreateRequest(
			"봄 맞이 할인 이벤트",
			"4월에 진행되는 예약금 할인 이벤트입니다.",
			"이벤트 시작 예정",
			UUID.randomUUID(),
			LocalDateTime.of(2025, 4, 8, 12, 0),
			LocalDateTime.of(2025, 4, 14, 18, 30)
		);

		// when
		PromotionCreateResponse response = promotionService.createEvent(request);

		// then
		assertThat(response.eventId()).isNotNull();
		System.out.println("response.eventId() = " + response.eventId());
	}
}
