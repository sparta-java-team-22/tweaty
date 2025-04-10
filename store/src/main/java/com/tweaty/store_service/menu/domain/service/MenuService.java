package com.tweaty.store_service.menu.domain.service;

import java.util.UUID;

import com.tweaty.store_service.menu.presentation.dto.requset.MenuRequestDto;

public interface MenuService {


	UUID createMenu(MenuRequestDto req, UUID storeId);
}
