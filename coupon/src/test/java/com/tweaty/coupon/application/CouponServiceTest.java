package com.tweaty.coupon.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.coupon.application.dto.CouponCreateResponse;
import com.tweaty.coupon.domain.model.Coupon;
import com.tweaty.coupon.domain.repository.CouponRepository;
import com.tweaty.coupon.domain.vo.CouponIssuancePeriod;
import com.tweaty.coupon.domain.vo.DiscountPolicy;
import com.tweaty.coupon.domain.vo.Quantity;
import com.tweaty.coupon.presentation.request.CouponCreateRequest;
import com.tweaty.coupon.presentation.request.CouponIssueRequest;

@SpringBootTest
@ActiveProfiles("test")
class CouponServiceTest {
	@Autowired
	private CouponService couponService;
	@Autowired
	private CouponRepository couponRepository;
	private Coupon coupon;

	@BeforeEach
	void setUp() {
		coupon = Coupon.builder()
			.couponName("3천원 할인 쿠폰")
			.discountPolicy(DiscountPolicy.from("정액", 3000))
			.couponMaxIssuance(Quantity.from(100))
			.couponRemainingStock(Quantity.from(100))
			.couponIssuancePeriod(CouponIssuancePeriod.of(
				LocalDateTime.of(2025, 4, 11, 11, 0),
				LocalDateTime.of(2025, 5, 10, 14, 0)
			))
			.couponValidUntil(LocalDateTime.of(2025, 5, 10, 14, 0))
			.isFirstCome(true)
			.build();

		couponRepository.save(coupon);
	}

	@Test
	@Transactional
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

	@Test
	public void issueCouponTest() throws InterruptedException {
		// given
		CouponIssueRequest request = new CouponIssueRequest(
			LocalDateTime.of(2025, 5, 10, 14, 0)
		);

		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);

		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		// when
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					couponService.issueCoupon(coupon.getCouponId(), request);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		executorService.shutdown();

		countDownLatch.await();

		// then
		Coupon updatedCoupon = couponRepository.findByCouponId(coupon.getCouponId()).get();

		assertThat(updatedCoupon.getCouponMaxIssuance().getValue()).isEqualTo(100);
		assertThat(updatedCoupon.getCouponRemainingStock().getValue()).isEqualTo(0);
	}
}
