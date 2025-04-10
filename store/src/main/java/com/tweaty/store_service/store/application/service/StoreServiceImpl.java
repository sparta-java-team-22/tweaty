package com.tweaty.store_service.store.application.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.store_service.store.domain.entity.Store;
import com.tweaty.store_service.store.domain.repository.StoreRepository;
import com.tweaty.store_service.store.global.exception.CustomException;
import com.tweaty.store_service.store.presentation.dto.request.StoreRequestDto;
import com.tweaty.store_service.store.presentation.dto.response.StoreResponseDto;

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

	private final StoreRepository storeRepository;

	// TODO: 전체 메서드 권한체크 필요
	@Override
	@Transactional
	public UUID createStore(StoreRequestDto req, UUID userId) {

		Store store = req.toEntity(userId);

		storeRepository.save(store);
		return store.getId();
	}

	@Override
	@Transactional
	public void updateStore(StoreRequestDto req, UUID storeId) {
		Store store = findStore(storeId);

		store.update(req);
	}

	@Override
	@Transactional
	public void deleteStore(UUID storeId) {
		Store store = findStore(storeId);

		store.softDelete();
	}

	@Override
	@Transactional(readOnly = true)
	public StoreResponseDto getStore(UUID storeId) {
		Store store = findStore(storeId);

		return StoreResponseDto.toDto(store);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StoreResponseDto> getStoreList(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		return storeRepository.findByIsDeletedIsFalse(pageable).map(StoreResponseDto::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StoreResponseDto> getStoreListByOwner(int page, int size, UUID userId) {
		Pageable pageable = PageRequest.of(page, size);

		return storeRepository.findByUserIdAndIsDeletedIsFalse(userId,pageable).map(StoreResponseDto::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StoreResponseDto> searchStoreList(String name, Boolean isReservation, Boolean isWaiting, int page,
		int size) {
		Pageable pageable = PageRequest.of(page, size);

		return storeRepository.searchStoreList(name, isReservation, isWaiting, pageable).map(StoreResponseDto::toDto);
	}

	public Store findStore(UUID storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (store.getIsDeleted()) {
			throw new CustomException(ErrorCode.STORE_ALREADY_DELETED, HttpStatus.BAD_REQUEST);
		}
		return store;
	}

}
