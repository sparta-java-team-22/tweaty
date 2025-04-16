package com.tweaty.user.application.dto;

import java.util.UUID;

import com.tweaty.user.domain.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateResponseDto {

	private UUID id;
	private String username;
	private String name;
	private String phoneNumber;
	private String email;

	public UserUpdateResponseDto(User user) {

		this.id = user.getId();
		this.username = user.getUsername();
		this.name = user.getName();
		this.phoneNumber = user.getPhoneNumber();
		this.email = user.getEmail();

	}
}