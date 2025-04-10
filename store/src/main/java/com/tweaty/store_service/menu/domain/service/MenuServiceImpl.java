package com.tweaty.store_service.menu.domain.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.store_service.menu.domain.entity.Menu;
import com.tweaty.store_service.menu.domain.repository.MenuRepository;
import com.tweaty.store_service.menu.presentation.dto.requset.MenuRequestDto;
import com.tweaty.store_service.store.application.service.StoreService;
import com.tweaty.store_service.store.domain.entity.Store;

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

}
