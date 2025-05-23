package com.tweaty.promotion.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.promotion.domain.vo.EventPeriod;
import com.tweaty.promotion.presentation.request.PromotionCreateRequest;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_promotion")
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
public class Promotion extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "promotion_id")
	private UUID promotionId;

	@Column(name = "coupon_id")
	private UUID couponId;

	@Column(name = "event_name")
	private String eventName;

	@Column(name = "event_description")
	private String eventDescription;

	@Enumerated(EnumType.STRING)
	@Column(name = "event_status")
	private EventStatus eventStatus;

	@Embedded
	private EventPeriod eventPeriod;

	public static Promotion create(PromotionCreateRequest request) {
		return Promotion.builder()
			.couponId(request.couponId())
			.eventName(request.eventName())
			.eventDescription(request.eventDescription())
			.eventStatus(EventStatus.from(request.eventStatus()))
			.eventPeriod(EventPeriod.of(request.eventStartAt(), request.eventEndAt()))
			.build();
	}

	public void updateEventStatusToEnded() {
		this.eventStatus = EventStatus.ENDED;
	}

	public void checkEventPeriod(LocalDateTime now) {
		eventPeriod.checkEventAvailable(now);
	}
}
