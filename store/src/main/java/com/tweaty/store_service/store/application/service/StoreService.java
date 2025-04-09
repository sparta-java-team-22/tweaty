package com.tweaty.store_service.store.application.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.store_service.store.domain.entity.Store;
import com.tweaty.store_service.store.domain.repository.StoreRepository;
import com.tweaty.store_service.store.presentation.dto.request.StoreRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;

	// TODO: 전체 메서드 권한체크 필요

	@Transactional
	public void createStore(StoreRequestDto req, UUID userId) {

		Store store = req.toEntity(userId);

		storeRepository.save(store);
	}

	@Transactional
	public void updateStore(StoreRequestDto req, UUID storeId) throws Exception {
		Store store = findStore(storeId);

		store.update(req);
	}

	@Transactional
	public void deleteStore(UUID storeId) throws Exception {
		Store store = findStore(storeId);

		store.softDelete();
	}

	public Store findStore(UUID storeId) throws Exception {
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new Exception("식당을 찾을 수 없습니다."));

		if (store.getIsDeleted()) {
			throw new Exception("삭제된 식당입니다.");
		}
		return store;
	}

}
