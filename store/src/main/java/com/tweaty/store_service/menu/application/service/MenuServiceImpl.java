package com.tweaty.store_service.menu.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.store_service.menu.application.service.MenuService;
import com.tweaty.store_service.menu.domain.entity.Menu;
import com.tweaty.store_service.menu.domain.repository.MenuRepository;
import com.tweaty.store_service.menu.presentation.dto.requset.MenuRequestDto;
import com.tweaty.store_service.menu.presentation.dto.response.MenuResponseDto;
import com.tweaty.store_service.store.application.service.StoreService;
import com.tweaty.store_service.store.domain.entity.Store;
import com.tweaty.store_service.store.global.exception.CustomException;

import domain.Role;
import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

	private final MenuRepository menuRepository;
	private final StoreService storeService;

	@Override
	@Transactional
	public UUID createMenu(MenuRequestDto req, UUID storeId, UUID userId, String role) {
		roleCheck(role);
		Store store = storeService.findStore(storeId, userId, role);
		Menu menu = req.toEntity(store);

		menuRepository.save(menu);

		return menu.getId();
	}

	@Override
	@Transactional
	public void updateMenu(MenuRequestDto req, UUID menuId, UUID userId, String role) {
		roleCheck(role);
		Menu menu = findMenu(menuId, userId, role);
		menu.update(req);
	}

	@Override
	@Transactional
	public void deleteMenu(UUID menuId, UUID userId, String role) {
		roleCheck(role);
		Menu menu = findMenu(menuId, userId, role);
		menu.softDelete();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<MenuResponseDto> getMenuList(int page, int size, UUID storeId) {

		Pageable pageable = PageRequest.of(page, size);

		storeService.findStore(storeId);

		return menuRepository.findByStoreIdAndIsDeletedIsFalse(storeId, pageable).map(MenuResponseDto::toDto);
	}

	private Menu findMenu(UUID menuId) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (menu.getIsDeleted()) {
			throw new CustomException(ErrorCode.MENU_ALREADY_DELETED, HttpStatus.BAD_REQUEST);
		}

		storeService.findStore(menu.getStore().getId());

		return menu;
	}

	public Menu findMenu(UUID menuId, UUID userId, String role) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (menu.getIsDeleted()) {
			throw new CustomException(ErrorCode.MENU_ALREADY_DELETED, HttpStatus.BAD_REQUEST);
		}

		storeService.findStore(menu.getStore().getId(), userId, role);

		return menu;
	}

	private void roleCheck(String role) {
		if (!role.equals(Role.ROLE_OWNER.name()) && !role.equals(Role.ROLE_ADMIN.name())) {
			throw new CustomException(ErrorCode.USER_FORBIDDEN, HttpStatus.FORBIDDEN);
		}
	}

}
