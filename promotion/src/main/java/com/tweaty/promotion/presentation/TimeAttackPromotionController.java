package com.tweaty.promotion.presentation;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.promotion.application.PromotionService;
import com.tweaty.promotion.application.dto.PromotionTimeAttackCouponAsyncResponse;

import constant.UserConstant;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/promotions")
public class TimeAttackPromotionController {
	private final PromotionService promotionService;

	@PostMapping("/{eventId}/issue")
	public ResponseEntity<PromotionTimeAttackCouponAsyncResponse> issueTimeAttackCouponV2(
		@PathVariable UUID eventId,
		@RequestHeader(UserConstant.X_USER_ID) UUID userId
	) {
		PromotionTimeAttackCouponAsyncResponse response = promotionService.issueTimeAttackCouponV2Async(eventId,
			userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
