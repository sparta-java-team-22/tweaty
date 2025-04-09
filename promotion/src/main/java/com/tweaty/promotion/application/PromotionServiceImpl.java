package com.tweaty.promotion.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.promotion.application.dto.PromotionCreateResponse;
import com.tweaty.promotion.application.dto.PromotionReadResponse;
import com.tweaty.promotion.domain.model.Promotion;
import com.tweaty.promotion.domain.repository.PromotionRepository;
import com.tweaty.promotion.exception.PromotionDuplicatedException;
import com.tweaty.promotion.exception.PromotionNotFoundException;
import com.tweaty.promotion.presentation.request.PromotionCreateRequest;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
	private final PromotionRepository promotionRepository;

	@Override
	@Transactional
	public PromotionCreateResponse createEvent(PromotionCreateRequest request) {
		// 쿠폰 존재 여부 확인

		if (promotionRepository.existsByEventName(request.eventName())) {
			throw new PromotionDuplicatedException(ErrorCode.PROMOTION_DUPLICATED);
		}

		Promotion promotion = Promotion.create(request);

		promotionRepository.save(promotion);

		return PromotionCreateResponse.from(promotion);
	}

	@Override
	@Transactional(readOnly = true)
	public PromotionReadResponse getEvent(UUID eventId) {
		Promotion promotion = promotionRepository.findByPromotionId(eventId)
			.orElseThrow(() -> new PromotionNotFoundException(ErrorCode.PROMOTION_NOT_FOUND));

		return PromotionReadResponse.from(promotion);
	}

	@Override
	@Transactional
	public void updateEventStatusToEnded(UUID eventId) {
		Promotion promotion = promotionRepository.findByPromotionId(eventId)
			.orElseThrow(() -> new PromotionNotFoundException(ErrorCode.PROMOTION_NOT_FOUND));

		promotion.updateEventStatusToEnded();
	}
}
