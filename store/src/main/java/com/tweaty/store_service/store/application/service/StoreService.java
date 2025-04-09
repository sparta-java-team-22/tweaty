package com.tweaty.store_service.store.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.tweaty.store_service.store.presentation.dto.request.StoreRequestDto;
import com.tweaty.store_service.store.presentation.dto.response.StoreResponseDto;

public interface StoreService {

	void createStore(StoreRequestDto req, UUID userId);

	void updateStore(StoreRequestDto req, UUID storeId) throws Exception;

	void deleteStore(UUID storeId) throws Exception;

	StoreResponseDto getStore(UUID storeId) throws Exception;

	 Page<StoreResponseDto> getStoreList(int page, int size);

	 Page<StoreResponseDto> searchStoreList(String name,Boolean isReservation,Boolean isWaiting,int page, int size);

	}
