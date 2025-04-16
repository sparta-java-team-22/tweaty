package com.tweaty.auth.application.dto;

import java.util.UUID;

import domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private UUID id;
	private String username;
	private String password;
	private Role role;

}
