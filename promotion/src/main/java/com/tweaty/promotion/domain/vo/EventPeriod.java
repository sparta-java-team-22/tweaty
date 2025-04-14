package com.tweaty.promotion.domain.vo;

import java.time.LocalDateTime;

import com.tweaty.promotion.exception.PromotionPeriodViolationException;

import exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class EventPeriod {
	@Column(name = "event_start_at")
	private LocalDateTime startAt;

	@Column(name = "event_end_at")
	private LocalDateTime endAt;

	public EventPeriod(LocalDateTime startAt, LocalDateTime endAt) {
		if (startAt == null || endAt == null || startAt.isAfter(endAt)) {
			throw new IllegalArgumentException("시작일은 종료일보다 앞서야 합니다.");
		}
		this.startAt = startAt;
		this.endAt = endAt;
	}

	public static EventPeriod of(LocalDateTime startAt, LocalDateTime endAt) {
		return new EventPeriod(startAt, endAt);
	}

	public void checkEventAvailable(LocalDateTime now) {
		if (now.isBefore(startAt) || now.isAfter(endAt)) {
			throw new PromotionPeriodViolationException(ErrorCode.PROMOTION_NOT_ACTIVE);
		}
	}
}
