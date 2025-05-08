package com.tweaty.reservation.presentation.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tweaty.reservation.application.dto.StoreResponseDto;

@FeignClient(name = "store-service")
public interface StoreClient {

	@GetMapping("/{storeId}")
	StoreResponseDto getStore(@PathVariable UUID storeId);

}
