package com.tweaty.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateRequestDto {

	String currentPassword;
	String newPassword;

}
