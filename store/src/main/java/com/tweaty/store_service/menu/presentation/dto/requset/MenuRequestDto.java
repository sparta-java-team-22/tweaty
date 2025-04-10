package com.tweaty.store_service.menu.presentation.dto.requset;

import java.util.UUID;

import com.tweaty.store_service.menu.domain.entity.CategoryType;
import com.tweaty.store_service.menu.domain.entity.Menu;
import com.tweaty.store_service.store.domain.entity.Store;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MenuRequestDto {
	@NotEmpty
	private String name;
	@NotEmpty
	private CategoryType category;
	private int price;
	private String imgUrl;
	private String description;

	@Builder
	public Menu toEntity(Store store) {
		return Menu.builder()
			.store(store)
			.name(this.name)
			.category(this.category)
			.price(this.price)
			.imgUrl(this.imgUrl)
			.description(this.description)
			.build();
	}

}


