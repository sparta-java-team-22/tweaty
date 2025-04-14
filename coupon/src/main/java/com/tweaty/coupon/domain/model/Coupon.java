package com.tweaty.coupon.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.coupon.domain.vo.CouponIssuancePeriod;
import com.tweaty.coupon.domain.vo.DiscountPolicy;
import com.tweaty.coupon.domain.vo.Quantity;
import com.tweaty.coupon.presentation.request.CouponCreateRequest;
import com.tweaty.coupon.presentation.request.CouponUpdateRequest;

import base.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
@Table(name = "p_coupon")
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
public class Coupon extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "coupon_id")
	private UUID couponId;

	@Column(name = "coupon_name")
	private String couponName;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "type", column = @Column(name = "discount_type")),
		@AttributeOverride(name = "amount", column = @Column(name = "discount_amount")),
	})
	private DiscountPolicy discountPolicy;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "value", column = @Column(name = "coupon_max_issuance"))
	})
	private Quantity couponMaxIssuance;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "value", column = @Column(name = "coupon_remaining_stock"))
	})
	private Quantity couponRemainingStock;

	@Embedded
	private CouponIssuancePeriod couponIssuancePeriod;

	@Column(name = "coupon_valid_until")
	private LocalDateTime couponValidUntil;

	@Column(name = "is_first_come")
	private Boolean isFirstCome;

	public static Coupon create(CouponCreateRequest request) {
		return Coupon.builder()
			.couponName(request.couponName())
			.discountPolicy(DiscountPolicy.from(request.discountType(), request.discountAmount()))
			.couponMaxIssuance(Quantity.from(request.couponMaxIssuance()))
			.couponRemainingStock(Quantity.from(request.couponMaxIssuance()))
			.couponIssuancePeriod(
				CouponIssuancePeriod.of(request.couponIssuanceStartAt(), request.couponIssuanceEndAt())
			)
			.couponValidUntil(request.couponIssuanceEndAt())
			.isFirstCome(request.isFirstCome())
			.build();
	}

	public void updateCouponRemainingStock() {
		couponRemainingStock.decreaseQuantity();
	}

	public void updateCoupon(CouponUpdateRequest request) {
		if (request.getCouponName() != null)
			this.couponName = request.getCouponName();
		if (request.getDiscountType() != null && request.getDiscountAmount() != null)
			this.discountPolicy = DiscountPolicy.from(
				request.getDiscountType(),
				request.getDiscountAmount()
			);
		if (request.getCouponMaxIssuance() != null)
			this.couponMaxIssuance = Quantity.from(request.getCouponMaxIssuance());
		if (request.getCouponIssuanceStartAt() != null && request.getCouponIssuanceEndAt() != null)
			this.couponIssuancePeriod = CouponIssuancePeriod.of(
				request.getCouponIssuanceStartAt(),
				request.getCouponIssuanceEndAt()
			);
		if (request.getIsFirstCome() != null)
			this.isFirstCome = request.getIsFirstCome();
	}
}
