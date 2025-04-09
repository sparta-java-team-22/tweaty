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

	@Transactional
	public void createStore(StoreRequestDto req, Long userId) {

		Store store = req.toEntity(userId);

		storeRepository.save(store);
	}

	@Transactional
	public void updateStore(StoreRequestDto req, Long userId, UUID storeId) throws Exception {
		Store store = findStore(storeId);

		store.update(req);
	}

	public Store findStore(UUID storeId) throws Exception {
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new Exception("상점을 찾을 수 없습니다."));

		// TODO: 삭제된 업체인지 검증

		return store;
	}

}
