package com.tweaty.store;


import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tweaty.store_service.store.application.service.StoreServiceImpl;
import com.tweaty.store_service.store.domain.entity.Store;
import com.tweaty.store_service.store.domain.repository.StoreRepository;
import com.tweaty.store_service.store.global.exception.CustomException;
import com.tweaty.store_service.store.presentation.dto.response.StoreResponseDto;

class StoreServiceImplTest {

	@Mock
	private StoreRepository storeRepository;

	@InjectMocks
	private StoreServiceImpl storeService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@DisplayName("가게 조회 성공")
	@Test
	void testGetStoreSuccess() {
		// given
		UUID storeId = UUID.randomUUID();
		Store store = Store.builder()
			.id(storeId)
			.name("테스트 가게")
			.address("서울시 강남구")
			.phoneNumber("010-1234-5678")
			.openTime("09:00")
			.closedTime("22:00")
			.status(null)
			.isReservation(true)
			.isWaiting(false)
			.reservationAmount(0)
			.build();

		when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

		// when
		StoreResponseDto response = storeService.getStore(storeId);

		// then
		assertThat(response.getId()).isEqualTo(storeId);
		assertThat(response.getName()).isEqualTo("테스트 가게");
		assertThat(response.getAddress()).isEqualTo("서울시 강남구");
	}

	@DisplayName("가게 조회 실패 - 존재하지 않는 가게")
	@Test
	void testGetStoreFail_NotFound() {
		// given
		UUID storeId = UUID.randomUUID();
		when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> storeService.getStore(storeId))
			.isInstanceOf(CustomException.class);
	}
}