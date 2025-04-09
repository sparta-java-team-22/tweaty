package com.tweaty.promotion.domain.model;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventStatus {
	READY("이벤트 시작 예정"),
	ACTIVE("이벤트 진행 중"),
	ENDED("이벤트 종료"),
	NOME("찾을 수 없음");

	private final String description;

	public static EventStatus from(String eventStatus) {
		return Arrays.stream(values())
			.filter(v -> v.description.equalsIgnoreCase(eventStatus.trim()))
			.findFirst()
			.orElse(NOME);
	}
}
