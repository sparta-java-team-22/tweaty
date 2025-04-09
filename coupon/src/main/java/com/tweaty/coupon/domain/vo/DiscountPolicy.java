package com.tweaty.coupon.domain.vo;

import com.tweaty.coupon.domain.model.DiscountType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountPolicy {
	@Enumerated(EnumType.STRING)
	@Column(name = "discount_type")
	private DiscountType type;

	@Column(name = "discount_amount")
	private Integer amount;

	public static DiscountPolicy from(String type, Integer amount) {
		return new DiscountPolicy(DiscountType.from(type), amount);
	}

	public void validate() {
		if (type == DiscountType.RATE && (amount < 1 || amount > 100)) {
			throw new IllegalArgumentException("퍼센트 할인은 1~100 사이여야 합니다.");
		}
		if (type == DiscountType.FIXED && amount <= 0) {
			throw new IllegalArgumentException("금액 할인은 0보다 커야 합니다.");
		}
	}
}
