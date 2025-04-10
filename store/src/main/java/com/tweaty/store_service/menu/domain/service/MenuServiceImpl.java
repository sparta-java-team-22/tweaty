package com.tweaty.store_service.menu.domain.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.store_service.menu.domain.entity.Menu;
import com.tweaty.store_service.menu.domain.repository.MenuRepository;
import com.tweaty.store_service.menu.presentation.dto.requset.MenuRequestDto;
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


	private Menu findMenu(UUID menuId) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND, HttpStatus.NOT_FOUND));

		if (menu.getIsDeleted()) {
			throw new CustomException(ErrorCode.MENU_ALREADY_DELETED, HttpStatus.BAD_REQUEST);
		}
		return menu;
	}

}
