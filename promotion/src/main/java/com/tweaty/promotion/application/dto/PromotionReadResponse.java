package com.tweaty.promotion.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.promotion.domain.model.EventStatus;
import com.tweaty.promotion.domain.model.Promotion;

public record PromotionReadResponse(
	UUID eventId,
	String eventName,
	String eventDescription,
	EventStatus eventStatus,
	UUID couponId,
	LocalDateTime eventStartAt,
	LocalDateTime eventEndAt
) {
	public static PromotionReadResponse from(Promotion promotion) {
		return new PromotionReadResponse(
			promotion.getPromotionId(),
			promotion.getEventName(),
			promotion.getEventDescription(),
			promotion.getEventStatus(),
			promotion.getCouponId(),
			promotion.getEventPeriod().getStartAt(),
			promotion.getEventPeriod().getEndAt()
		);
	}
}
