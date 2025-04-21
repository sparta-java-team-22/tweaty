package com.tweaty.store_service.menu.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.tweaty.store_service.menu.presentation.dto.requset.MenuRequestDto;
import com.tweaty.store_service.menu.presentation.dto.response.MenuResponseDto;

public interface MenuService {

	UUID createMenu(MenuRequestDto req, UUID storeId, UUID userId, String role);

	void updateMenu(MenuRequestDto req, UUID menuId, UUID userId, String role);

	void deleteMenu(UUID menuId, UUID userId, String role);

	Page<MenuResponseDto> getMenuList(int page, int size, UUID storeId);

}
