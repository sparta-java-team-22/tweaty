package com.tweaty.auth.application.dto;

import domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {

	private String username;
	private String password;
	private String name;
	private String phoneNumber;
	private String email;
	private Role role;

}
