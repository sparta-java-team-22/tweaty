package com.tweaty.store_service.store.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.store_service.store.application.service.StoreService;
import com.tweaty.store_service.store.presentation.dto.request.StoreRequestDto;
import com.tweaty.store_service.store.presentation.dto.response.StoreListResponse;
import com.tweaty.store_service.store.presentation.dto.response.StoreResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vi/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@PostMapping
	public ResponseEntity<?> createStore(@RequestBody StoreRequestDto req) {

		// TODO: 유저아이디 받아오기
		UUID userId = UUID.randomUUID();

		storeService.createStore(req, userId);

		return ResponseEntity.status(HttpStatus.CREATED).body("식당 생성 성공");
	}

	@PatchMapping("/{storeId}")
	public ResponseEntity<?> updateStore(@RequestBody StoreRequestDto req, @PathVariable UUID storeId) throws
		Exception {

		// TODO: 유저아이디 받아오기
		UUID userId = UUID.randomUUID();

		storeService.updateStore(req, storeId);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body("식당 수정 성공");
	}

	@DeleteMapping("/{storeId}")
	public ResponseEntity<?> deleteStore(@PathVariable UUID storeId) throws Exception {

		// TODO: 유저아이디 받아오기
		UUID userId = UUID.randomUUID();

		storeService.deleteStore(storeId);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body("식당 삭제 성공");
	}

	@GetMapping("/{storeId}")
	public StoreResponseDto getStore(@PathVariable UUID storeId) throws Exception {

		// TODO: 유저아이디 받아오기
		UUID userId = UUID.randomUUID();

		return storeService.getStore(storeId);
	}

	@GetMapping()
	public StoreListResponse getStoreList(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) throws Exception {

		// TODO: 유저아이디 받아오기
		UUID userId = UUID.randomUUID();

		Page<StoreResponseDto> storePage = storeService.getStoreList(page, size);
		return StoreListResponse.from(storePage);
	}

}
