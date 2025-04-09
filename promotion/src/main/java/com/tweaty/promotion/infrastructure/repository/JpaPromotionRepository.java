package com.tweaty.promotion.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweaty.promotion.domain.model.Promotion;
import com.tweaty.promotion.domain.repository.PromotionRepository;

public interface JpaPromotionRepository extends JpaRepository<Promotion, Long>, PromotionRepository {
	boolean existsByEventName(String eventName);

	Optional<Promotion> findByPromotionId(UUID eventId);
}
