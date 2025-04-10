package com.tweaty.store_service.menu.domain.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryType {
	RESTAURANT("레스토랑"),
	MEAT("육류,고기요리"),
	SEAFOOD("해물,생선요리"),
	PUB("주점"),
	CAFE("카페,베이커리"),
	ETC("기타");


	private final String label;

	}
