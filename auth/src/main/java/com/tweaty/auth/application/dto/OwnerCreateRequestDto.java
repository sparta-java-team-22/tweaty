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
public class OwnerCreateRequestDto {

	private String username;
	private String password;
	private String name;
	private String phoneNumber;
	private String email;
	private Role role;
	private String businessNumber;
	private String businessLicenseUrl;

}
