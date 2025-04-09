package com.tweaty.promotion.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.tweaty.promotion.domain.model.Promotion;

public interface PromotionRepository {
	boolean existsByEventName(String eventName);

	Promotion save(Promotion promotion);

	Optional<Promotion> findByPromotionId(UUID eventId);
}
