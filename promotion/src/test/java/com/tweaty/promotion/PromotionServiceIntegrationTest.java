package com.tweaty.promotion;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweaty.promotion.application.dto.PromotionTimeAttackCouponResponse;
import com.tweaty.promotion.domain.model.EventStatus;
import com.tweaty.promotion.domain.model.Promotion;
import com.tweaty.promotion.domain.repository.PromotionRepository;
import com.tweaty.promotion.domain.vo.EventPeriod;
import com.tweaty.promotion.infrastructure.client.CouponServiceClient;
import com.tweaty.promotion.infrastructure.client.dto.CouponIssueRequest;
import com.tweaty.promotion.infrastructure.client.dto.CouponIssueResponse;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PromotionServiceIntegrationTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PromotionRepository promotionRepository;
	@MockitoBean
	private CouponServiceClient couponServiceClient;
	private Promotion promotion;

	@BeforeEach
	void setUp() {
		promotion = Promotion.builder()
			.eventName("5월 이벤트")
			.eventDescription("5월 진행되는 이벤트입니다.")
			.eventStatus(EventStatus.READY)
			.couponId(UUID.randomUUID())
			.eventPeriod(EventPeriod.of(
					LocalDateTime.of(2025, 4, 11, 12, 0),
					LocalDateTime.of(2025, 4, 20, 23, 59)
				)
			)
			.build();

		promotionRepository.save(promotion);

		Mockito.when(couponServiceClient.issueCoupon(
				Mockito.eq(promotion.getCouponId()),
				Mockito.any(CouponIssueRequest.class)
			))
			.thenReturn(new CouponIssueResponse(
				UUID.randomUUID(),
				LocalDateTime.now(),
				LocalDateTime.now().plusDays(7)
			));
	}

	@Test
	public void issueTimeAttackCouponTest() throws Exception {
		// given
		UUID eventId = promotion.getPromotionId();

		// when
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.post("/api/v1/promotions/" + eventId + "/issue")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andReturn();

		// then
		String json = result.getResponse().getContentAsString();

		PromotionTimeAttackCouponResponse response = objectMapper.readValue(
			json,
			PromotionTimeAttackCouponResponse.class
		);

		assertNotNull(response.couponIssueId());
		assertNotNull(response.couponIssuedAt());
		assertNotNull(response.couponExpiredAt());
		assertTrue(response.isCouponIssued());

		Mockito.verify(couponServiceClient, Mockito.times(1))
			.issueCoupon(Mockito.any(), Mockito.any());
	}
}
