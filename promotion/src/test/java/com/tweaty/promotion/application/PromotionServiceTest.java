package com.tweaty.promotion.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.promotion.application.dto.PromotionCreateResponse;
import com.tweaty.promotion.application.dto.PromotionReadResponse;
import com.tweaty.promotion.domain.model.EventStatus;
import com.tweaty.promotion.domain.model.Promotion;
import com.tweaty.promotion.domain.repository.PromotionRepository;
import com.tweaty.promotion.presentation.request.PromotionCreateRequest;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PromotionServiceTest {
	@Autowired
	private PromotionService promotionService;
	@Autowired
	private PromotionRepository promotionRepository;
	private Promotion testPromotion;

	@BeforeEach
	void setUp() {
		testPromotion = Promotion.builder()
			.eventName("5월 이벤트")
			.eventDescription("5월 진행되는 이벤트입니다.")
			.eventStatus(EventStatus.READY)
			.couponId(UUID.randomUUID())
			.eventStartAt(LocalDateTime.of(2025, 5, 1, 12, 0))
			.eventEndAt(LocalDateTime.of(2025, 5, 8, 23, 59))
			.build();

		promotionRepository.save(testPromotion);
	}

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

	@Test
	public void getPromotionTest() {
		// given
		// when
		PromotionReadResponse response = promotionService.getEvent(testPromotion.getPromotionId());
		// then
		assertThat(response.eventId()).isNotNull();
		assertThat(response.eventId()).isEqualTo(testPromotion.getPromotionId());
		assertThat(response.eventName()).isEqualTo(testPromotion.getEventName());
		assertThat(response.eventStatus()).isEqualTo(testPromotion.getEventStatus());
	}

	@Test
	public void updateEventStatusToEndedTest() {
		// given
		// when
		promotionService.updateEventStatusToEnded(testPromotion.getPromotionId());
		// then
		//Promotion updated = promotionRepository.findByPromotionId(testPromotion.getPromotionId()).get();

		assertThat(testPromotion.getEventStatus()).isEqualTo(EventStatus.ENDED);
	}
}
