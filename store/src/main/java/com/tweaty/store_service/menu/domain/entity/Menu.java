package com.tweaty.store_service.menu.domain.entity;

import java.awt.*;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tweaty.store_service.menu.presentation.dto.requset.MenuRequestDto;
import com.tweaty.store_service.store.domain.entity.Store;
import com.tweaty.store_service.store.presentation.dto.request.StoreRequestDto;

import base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_menu")
public class Menu extends BaseEntity {
	@Id
	@UuidGenerator
	@Column(name = "menu_id")
	private UUID id;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(nullable = false)
	private String name;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CategoryType category;
	@ColumnDefault("0")
	private int price;
	private String imgUrl;
	private String description;

	public void update(MenuRequestDto req) {
		this.name = req.getName();
		this.category = req.getCategory();
		this.price = req.getPrice();
		this.imgUrl = req.getImgUrl();
		this.description = req.getDescription();
	}

}
