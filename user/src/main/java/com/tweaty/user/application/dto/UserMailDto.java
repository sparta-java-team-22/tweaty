package com.tweaty.user.application.dto;

import com.tweaty.user.domain.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMailDto {

	private String email;

	public UserMailDto(User user) {
		this.email = user.getEmail();
	}
}
