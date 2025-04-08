package com.tweaty.store_service.store.application.service;

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

}
