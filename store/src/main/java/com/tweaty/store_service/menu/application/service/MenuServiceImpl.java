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

import exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

	private final MenuRepository menuRepository;
	private final StoreService storeService;

	@Override
	@Transactional
	public UUID createMenu(MenuRequestDto req, UUID storeId) {

		Store store = storeService.findStore(storeId);
		Menu menu = req.toEntity(store);

		menuRepository.save(menu);

		return menu.getId();
	}

	@Override
	@Transactional
	public void updateMenu(MenuRequestDto req, UUID menuId) {
		Menu menu = findMenu(menuId);
		menu.update(req);
	}


	@Override
	@Transactional
	public void deleteMenu(UUID menuId) {
		Menu menu = findMenu(menuId);
		menu.softDelete();
	}


	@Override
	@Transactional(readOnly = true)
	public Page<MenuResponseDto> getMenuList(int page, int size,UUID storeId) {

		Pageable pageable = PageRequest.of(page, size);

		storeService.findStore(storeId);

		return menuRepository.findByStoreIdAndIsDeletedIsFalse(storeId,pageable).map(MenuResponseDto::toDto);
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

}
