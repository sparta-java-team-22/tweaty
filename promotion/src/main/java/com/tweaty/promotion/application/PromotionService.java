package com.tweaty.promotion.application;

import org.springframework.stereotype.Service;

import com.tweaty.promotion.application.dto.PromotionCreateResponse;
import com.tweaty.promotion.domain.model.Promotion;
import com.tweaty.promotion.domain.repository.PromotionRepository;
import com.tweaty.promotion.exception.PromotionDuplicatedException;
import com.tweaty.promotion.presentation.request.PromotionCreateRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionService {
	private final PromotionRepository promotionRepository;

	public PromotionCreateResponse createEvent(PromotionCreateRequest request) {
		// 쿠폰 존재 여부 확인

		if (promotionRepository.existsByEventName(request.eventName())) {
			throw new PromotionDuplicatedException(ErrorCode.PROMOTION_DUPLICATED);
		}

		Promotion promotion = Promotion.create(request);

		promotionRepository.save(promotion);

		return PromotionCreateResponse.from(promotion);
	}
}
