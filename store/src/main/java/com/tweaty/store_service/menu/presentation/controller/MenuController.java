package com.tweaty.store_service.menu.presentation.controller;

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

import com.tweaty.store_service.menu.application.dto.MenuIdDto;
import com.tweaty.store_service.menu.application.service.MenuService;
import com.tweaty.store_service.menu.presentation.dto.requset.MenuRequestDto;
import com.tweaty.store_service.menu.presentation.dto.response.MenuListResponse;
import com.tweaty.store_service.menu.presentation.dto.response.MenuResponseDto;
import com.tweaty.store_service.store.global.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;

	@PostMapping("/{storeId}")
	public ResponseEntity<?> createMenu(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @RequestBody MenuRequestDto req, @PathVariable UUID storeId) {

		MenuIdDto menuIdDto = new MenuIdDto(menuService.createMenu(req, storeId,userId, role));

		return SuccessResponse.successWith(200, "메뉴 생성 성공", menuIdDto);

	}

	@PatchMapping("/{menuId}")
	public ResponseEntity<?> updateMenu(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @RequestBody MenuRequestDto req, @PathVariable UUID menuId) {

		menuService.updateMenu(req, menuId, userId, role);
		return SuccessResponse.successMessageOnly("메뉴 수정 성공");
	}

	@DeleteMapping("/{menuId}")
	public ResponseEntity<?> deleteMenu(@RequestHeader("X-USER-ID") UUID userId,
		@RequestHeader("X-USER-ROLE") String role, @PathVariable UUID menuId) {

		menuService.deleteMenu(menuId, userId, role);
		return SuccessResponse.successMessageOnly("메뉴 삭제 성공");
	}

	@GetMapping("/{storeId}")
	public ResponseEntity<?> getMenuList(@PathVariable UUID storeId, @RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		Page<MenuResponseDto> menuPage = menuService.getMenuList(page, size, storeId);

		return SuccessResponse.successWith(200, "메뉴 전체조회 성공.", MenuListResponse.from(menuPage));
	}

}
