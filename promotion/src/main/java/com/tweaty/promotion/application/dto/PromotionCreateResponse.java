package com.tweaty.promotion.application.dto;

import java.util.UUID;

import com.tweaty.promotion.domain.model.Promotion;

public record PromotionCreateResponse(UUID eventId) {
	public static PromotionCreateResponse from(Promotion promotion) {
		return new PromotionCreateResponse(promotion.getPromotionId());
	}
}
