package com.tweaty.coupon.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Quantity {
	@Column(name = "value")
	private Integer value;

	public Quantity(Integer value) {
		if (value == null || value < 0) {
			throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
		}
		this.value = value;
	}

	public static Quantity from(Integer value) {
		return new Quantity(value);
	}
}
