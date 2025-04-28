package com.tweaty.payment.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;

import com.tweaty.payment.infrastucture.kafka.event.PaymentCreateEvent;
import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_payment")
public class Payment extends BaseEntity {
	@Id
	@UuidGenerator
	@Column(name = "payment_id")
	private UUID id;
	@Column(nullable = false)
	private UUID userId;
	@Column(nullable = false)
	private UUID reservationId;
	private UUID couponId;
	@Column(nullable = false)
	@ColumnDefault("0")
	private int originalAmount;
	@ColumnDefault("0")
	private int discountAmount;
	@ColumnDefault("0")
	private int finalAmount;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentType status;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MethodType method;

	public static Payment toReadyEntity(PaymentRequestDto req, UUID reservationId, UUID userId) {
		return Payment.builder()
			.userId(userId)
			.reservationId(reservationId)
			.couponId(req.getCouponId())
			.originalAmount(req.getOriginalAmount())
			.finalAmount(req.getOriginalAmount())
			.method(req.getMethod())
			.status(PaymentType.READY)
			.build();
	}

	public void applyDiscount(int discountAmount, int finalAmount) {
		this.discountAmount = discountAmount;
		this.finalAmount = finalAmount;
	}

	public void successPayment() {
		this.status = PaymentType.SUCCESS;
	}

	public void failPayment() {
		this.status = PaymentType.FAIL;
	}

	public void successRefund() {
		this.status = PaymentType.REFUNDED;
	}

	public static Payment toReadyEntity(PaymentCreateEvent event) {
		return Payment.builder()
			.userId(event.getUserId())
			.reservationId(event.getReservationId())
			.originalAmount(event.getOriginalAmount())
			.status(PaymentType.READY)
			.method(MethodType.valueOf(event.getMethod()))
			.couponId(event.getCouponId())
			.build();
	}

	public static Payment toFailedEntity(PaymentCreateEvent event) {
		return Payment.builder()
			.userId(event.getUserId())
			.reservationId(event.getReservationId())
			.originalAmount(event.getOriginalAmount())
			.status(PaymentType.FAIL)
			.method(MethodType.valueOf(event.getMethod()))
			.couponId(event.getCouponId())
			.build();
	}

	public boolean isCompleted() {
		return this.status == PaymentType.SUCCESS || this.status == PaymentType.FAIL;
	}

}
