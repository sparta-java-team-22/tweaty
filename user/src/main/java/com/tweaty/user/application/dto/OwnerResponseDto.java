package com.tweaty.user.application.dto;

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
public class OwnerResponseDto {

	private UUID id;
	private String username;
	private Role role;
	private String approvalStatus;

}
