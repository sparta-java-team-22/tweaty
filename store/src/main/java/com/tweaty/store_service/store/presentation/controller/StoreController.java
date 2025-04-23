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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.store_service.store.application.dto.StoreIdDto;
import com.tweaty.store_service.store.application.service.StoreService;

import com.tweaty.store_service.store.presentation.dto.request.StoreRequestDto;
import com.tweaty.store_service.store.presentation.dto.response.StoreListResponse;
import com.tweaty.store_service.store.presentation.dto.response.StoreResponseDto;

import lombok.RequiredArgsConstructor;
import response.SuccessResponse;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@PostMapping
	public ResponseEntity<?> createStore(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @RequestBody StoreRequestDto req) {

		StoreIdDto storeIdDto = new StoreIdDto(storeService.createStore(req, userId, role));
		return SuccessResponse.successWith(200, "식당 생성 성공.", storeIdDto);
	}

	@PatchMapping("/{storeId}")
	public ResponseEntity<?> updateStore(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @RequestBody StoreRequestDto req, @PathVariable UUID storeId) {

		storeService.updateStore(req, storeId,userId,role);

		return SuccessResponse.successMessageOnly("식당 수정 성공");
	}

	@DeleteMapping("/{storeId}")
	public ResponseEntity<?> deleteStore(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @PathVariable UUID storeId) {

		storeService.deleteStore(storeId,userId,role);

		return SuccessResponse.successMessageOnly("식당 삭제 성공");
	}

	// TODO: 예약서비스에서 요청으로 사용
	@GetMapping("/{storeId}")
	public StoreResponseDto getStore( @PathVariable UUID storeId) {
		return storeService.getStore(storeId);
	}

	@GetMapping
	public ResponseEntity<?> getStoreList(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		Page<StoreResponseDto> storePage = storeService.getStoreList(page, size);

		return SuccessResponse.successWith(200, "식당 전체조회 성공.", StoreListResponse.from(storePage));
	}

	// TODO: 메뉴관련해서 검색조건 추가예정 // 현재는 : 식당이름 키워드만 기준
	@GetMapping("/search")
	public ResponseEntity<?> searchStoreList(
		@RequestParam(required = false) String name,
		@RequestParam(defaultValue = "false") Boolean isReservation,
		@RequestParam(defaultValue = "false") Boolean isWaiting,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {


		Page<StoreResponseDto> storePage = storeService.searchStoreList(name, isReservation, isWaiting, page, size);

		return SuccessResponse.successWith(200, "식당 검색 성공.", StoreListResponse.from(storePage));
	}

	@GetMapping("/owner")
	public ResponseEntity<?> getStoreListByOwner(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		Page<StoreResponseDto> storePage = storeService.getStoreListByOwner(page, size, userId);

		return SuccessResponse.successWith(200, "식당 조회 성공.", StoreListResponse.from(storePage));
	}

}
