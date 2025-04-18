package com.tweaty.auth.application.dto;

import java.util.UUID;

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

	public UserResponseDto(UserDto user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.role = user.getRole();
	}

}
