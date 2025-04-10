package com.tweaty.store_service.menu.presentation.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.store_service.menu.application.dto.MenuIdDto;
import com.tweaty.store_service.menu.domain.service.MenuService;
import com.tweaty.store_service.menu.presentation.dto.requset.MenuRequestDto;
import com.tweaty.store_service.store.global.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;

	@PostMapping("/{storeId}")
	public ResponseEntity<?> createMenu(@RequestBody MenuRequestDto req,@PathVariable UUID storeId)  {

		MenuIdDto menuIdDto = new MenuIdDto( menuService.createMenu(req,storeId));

		return SuccessResponse.successWith(200,"메뉴 생성 성공",menuIdDto);

	}

	@PatchMapping("/{menuId}")
	public ResponseEntity<?> updateMenu(@RequestBody MenuRequestDto req,@PathVariable UUID menuId)  {

		menuService.updateMenu(req,menuId);
		return SuccessResponse.successMessageOnly("메뉴 수정 성공");
	}

	@DeleteMapping("/{menuId}")
	public ResponseEntity<?> deleteMenu(@PathVariable UUID menuId)  {

		menuService.deleteMenu(menuId);
		return SuccessResponse.successMessageOnly("메뉴 삭제 성공");

	}
}
