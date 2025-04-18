package com.tweaty.user.application.dto;

import java.util.UUID;

import domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {

	private UUID id;
	private String username;
	private String password;
	private Role role;

}
