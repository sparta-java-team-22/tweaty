package com.tweaty.promotion.application;

import java.util.UUID;

import com.tweaty.promotion.application.dto.PromotionCreateResponse;
import com.tweaty.promotion.application.dto.PromotionReadResponse;
import com.tweaty.promotion.application.dto.PromotionTimeAttackCouponAsyncResponse;
import com.tweaty.promotion.application.dto.PromotionTimeAttackCouponSyncResponse;
import com.tweaty.promotion.presentation.request.PromotionCreateRequest;

public interface PromotionService {
	PromotionCreateResponse createEvent(PromotionCreateRequest request);

	PromotionReadResponse getEvent(UUID eventId);

	void updateEventStatusToEnded(UUID eventId);

	PromotionTimeAttackCouponSyncResponse issueTimeAttackCouponV1Sync(UUID eventId);

	PromotionTimeAttackCouponAsyncResponse issueTimeAttackCouponV2Async(UUID eventId, UUID userId);
}
