package com.tweaty.promotion.domain.repository;

import com.tweaty.promotion.domain.model.Promotion;

public interface PromotionRepository {
	boolean existsByEventName(String eventName);

	Promotion save(Promotion promotion);
}
