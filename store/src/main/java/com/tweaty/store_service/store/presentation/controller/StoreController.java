package com.tweaty.store_service.store.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.store_service.store.application.dto.StoreIdDto;
import com.tweaty.store_service.store.application.service.StoreService;

import com.tweaty.store_service.store.global.SuccessResponse;
import com.tweaty.store_service.store.presentation.dto.request.StoreRequestDto;
import com.tweaty.store_service.store.presentation.dto.response.StoreListResponse;
import com.tweaty.store_service.store.presentation.dto.response.StoreResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@PostMapping
	public ResponseEntity<?> createStore(@RequestBody StoreRequestDto req) {

		// TODO: 유저아이디 받아오기
		UUID userId = UUID.randomUUID();

		StoreIdDto storeIdDto = new StoreIdDto(storeService.createStore(req, userId));
		return SuccessResponse.successWith(200, "식당 생성 성공.",storeIdDto);
	}

	@PatchMapping("/{storeId}")
	public ResponseEntity<?> updateStore(@RequestBody StoreRequestDto req, @PathVariable UUID storeId) {

		// TODO: 유저아이디 받아오기
		UUID userId = UUID.randomUUID();

		storeService.updateStore(req, storeId);

		return SuccessResponse.successMessageOnly("식당 수정 성공");
	}

	@DeleteMapping("/{storeId}")
	public ResponseEntity<?> deleteStore(@PathVariable UUID storeId) {

		// TODO: 유저아이디 받아오기
		UUID userId = UUID.randomUUID();

		storeService.deleteStore(storeId);

		return SuccessResponse.successMessageOnly("식당 삭제 성공");
	}

	@GetMapping("/{storeId}")
	public ResponseEntity<?> getStore(@PathVariable UUID storeId) {

		// TODO: 유저아이디 받아오기
		UUID userId = UUID.randomUUID();
		return SuccessResponse.successWith(200, "식당 단건조회 성공.", storeService.getStore(storeId));
	}

	@GetMapping()
	public ResponseEntity<?> getStoreList(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		// TODO: 유저아이디 받아오기
		UUID userId = UUID.randomUUID();

		Page<StoreResponseDto> storePage = storeService.getStoreList(page, size);

		return SuccessResponse.successWith(200, "식당 전체조회 성공.", StoreListResponse.from(storePage));
	}

	// TODO: 메뉴관련해서 검색조건 추가 // 현재는 : 식당이름 키워드만 기준
	@GetMapping("/search")
	public ResponseEntity<?> searchStoreList(
		@RequestParam(required = false) String name,
		@RequestParam(defaultValue = "false") Boolean isReservation,
		@RequestParam(defaultValue = "false") Boolean isWaiting,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		// TODO: 유저아이디 받아오기
		UUID userId = UUID.randomUUID();

		Page<StoreResponseDto> storePage = storeService.searchStoreList(name, isReservation, isWaiting, page, size);

		return SuccessResponse.successWith(200, "식당 검색 성공.", StoreListResponse.from(storePage));
	}


	// TODO: 추후 유저아이디 헤더에 넣기
	@GetMapping("/owner/{userId}")
	public ResponseEntity<?> getStoreListByOwner(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,@PathVariable UUID userId) {


		Page<StoreResponseDto> storePage = storeService.getStoreListByOwner(page, size,userId);

		return SuccessResponse.successWith(200, "식당 조회 성공.", StoreListResponse.from(storePage));
	}


}
