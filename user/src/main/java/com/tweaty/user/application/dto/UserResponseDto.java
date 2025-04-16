package com.tweaty.user.application.dto;

import java.util.UUID;

import com.tweaty.user.domain.model.User;

import domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

	private UUID id;
	private String username;
	private Role role;

	public UserResponseDto(User user) {

		this.id = user.getId();
		this.username = user.getUsername();
		this.role = user.getRole();

	}
}