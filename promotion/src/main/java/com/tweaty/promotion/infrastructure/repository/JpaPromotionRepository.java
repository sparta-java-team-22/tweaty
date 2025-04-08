package com.tweaty.promotion.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweaty.promotion.domain.model.Promotion;
import com.tweaty.promotion.domain.repository.PromotionRepository;

public interface JpaPromotionRepository extends JpaRepository<Promotion, Long>, PromotionRepository {
	boolean existsByEventName(String eventName);
}
