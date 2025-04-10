package com.tweaty.store_service.store.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.tweaty.store_service.store.domain.entity.Store;
import com.tweaty.store_service.store.presentation.dto.request.StoreRequestDto;
import com.tweaty.store_service.store.presentation.dto.response.StoreResponseDto;

public interface StoreService {

	UUID createStore(StoreRequestDto req, UUID userId);

	void updateStore(StoreRequestDto req, UUID storeId);

	void deleteStore(UUID storeId);

	StoreResponseDto getStore(UUID storeId);

	Page<StoreResponseDto> getStoreList(int page, int size);

	Page<StoreResponseDto> searchStoreList(String name, Boolean isReservation, Boolean isWaiting, int page, int size);


	Store findStore(UUID storeId);
}
