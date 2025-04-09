package com.tweaty.store_service.store.presentation.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.store_service.store.application.service.StoreService;
import com.tweaty.store_service.store.presentation.dto.request.StoreRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vi/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@PostMapping
	public ResponseEntity<?> createStore(@RequestBody StoreRequestDto req) {

		// TODO: 유저아이디 받아오기
		Long userId = 1L;

		storeService.createStore(req, userId);

		return ResponseEntity.status(HttpStatus.CREATED).body("가게 생성 성공");
	}

	@PatchMapping("/{storeId}")
	public ResponseEntity<?> updateStore(@RequestBody StoreRequestDto req,@PathVariable UUID storeId) throws Exception {

		// TODO: 유저아이디 받아오기
		Long userId = 1L;

		storeService.updateStore(req, userId,storeId);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body("가게 수정 성공");
	}


}
