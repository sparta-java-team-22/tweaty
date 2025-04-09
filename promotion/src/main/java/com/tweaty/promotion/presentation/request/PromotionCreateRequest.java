package com.tweaty.promotion.presentation.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record PromotionCreateRequest(
	String eventName,
	String eventDescription,
	String eventStatus,
	UUID couponId,
	LocalDateTime eventStartAt,
	LocalDateTime eventEndAt
) {
}
