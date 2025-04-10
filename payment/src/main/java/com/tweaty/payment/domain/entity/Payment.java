package com.tweaty.payment.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;

import com.tweaty.payment.presentation.dto.request.PaymentRequestDto;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
	private UUID storeId;
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

	public static Payment toReadyEntity(PaymentRequestDto req,UUID storeId,UUID userId){
		return Payment.builder()
			.userId(userId)
			.storeId(storeId)
			.couponId(req.getCouponId())
			.originalAmount(req.getOriginalAmount())
			.finalAmount(req.getOriginalAmount())
			.method(req.getMethod())
			.status(PaymentType.READY)
			.build();
	}

	public void applyDiscount(int discountAmount,int finalAmount) {
		this.discountAmount = discountAmount;
		this.finalAmount =finalAmount;
	}

	public void successPayment() {
		this.status = PaymentType.SUCCESS;
	}

	public void failPayment() {
		this.status = PaymentType.FAIL;
	}


}
