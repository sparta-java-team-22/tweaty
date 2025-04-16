package com.tweaty.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OwnerCreateRequestDto extends UserCreateRequestDto {

	private String businessNumber;
	private String businessLicenseUrl;

}