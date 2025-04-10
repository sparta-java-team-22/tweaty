package com.tweaty.store_service.menu.presentation.dto.response;

import java.util.UUID;

import com.tweaty.store_service.menu.domain.entity.CategoryType;
import com.tweaty.store_service.menu.domain.entity.Menu;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MenuResponseDto {

	@NotEmpty
	private UUID id;
	@NotEmpty
	private String name;
	@NotEmpty
	private CategoryType category;
	private int price;
	private String imgUrl;
	private String description;

	public static MenuResponseDto toDto(Menu menu) {
		return MenuResponseDto.builder()
			.id(menu.getId())
			.name(menu.getName())
			.category(menu.getCategory())
			.price(menu.getPrice())
			.imgUrl(menu.getImgUrl())
			.description(menu.getDescription())
			.build();
	}

}
