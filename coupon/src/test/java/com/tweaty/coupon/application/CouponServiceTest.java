package com.tweaty.coupon.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.coupon.application.dto.CouponCreateResponse;
import com.tweaty.coupon.presentation.request.CouponCreateRequest;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CouponServiceTest {
	@Autowired
	private CouponService couponService;

	@Test
	public void creatCouponTest() {
		// given
		CouponCreateRequest request = new CouponCreateRequest(
			"10% 할인 쿠폰",
			"정률",
			10,
			100,
			LocalDateTime.of(2025, 5, 1, 14, 0),
			LocalDateTime.of(2025, 5, 7, 14, 0),
			false
		);

		// when
		CouponCreateResponse response = couponService.createCoupon(request);

		// then
		assertThat(response).isNotNull();
		System.out.println("response.couponId() = " + response.couponId());
	}
}
