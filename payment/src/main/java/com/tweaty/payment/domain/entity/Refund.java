package com.tweaty.payment.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;

import com.tweaty.payment.presentation.dto.request.RefundRequestDto;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "p_refund")
public class Refund extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "refund_id")
	private UUID id;
	@Column(nullable = false)
	private UUID userId;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id", nullable = false)
	private Payment payment;

	@ColumnDefault("0")
	private int amount;

	private String reason;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private RefundType status;


	@Builder
	public static Refund toReadyEntity(RefundRequestDto req, UUID userId, Payment payment) {
		return Refund.builder().userId(userId).payment(payment).amount(payment.getFinalAmount()).reason(req.getReason()).status(RefundType.READY).build();
	}

	public void successRefund() {
		this.status = RefundType.SUCCESS;
	}

	public void failRefund() {
		this.status = RefundType.FAIL;
	}

}
