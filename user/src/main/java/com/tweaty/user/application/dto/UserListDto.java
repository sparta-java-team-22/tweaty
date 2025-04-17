package com.tweaty.user.application.dto;

import java.time.LocalDateTime;

import com.tweaty.user.domain.model.ApprovalStatus;
import com.tweaty.user.domain.model.Owner;
import com.tweaty.user.domain.model.User;

import domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserListDto {

	private String username;
	private String name;
	private String phoneNumber;
	private String email;
	private Role role;
	private LocalDateTime createdAt;

	//owner
	private String businessNumber;
	private String businessLicenseUrl;
	private ApprovalStatus approvalStatus;

	public UserListDto(User user) {

		this.username = user.getUsername();
		this.name = user.getName();
		this.phoneNumber = user.getPhoneNumber();
		this.email = user.getEmail();
		this.role = user.getRole();
		this.createdAt = user.getCreatedAt();

	}

	public UserListDto(Owner owner) {

		this.username = owner.getUser().getUsername();
		this.name = owner.getUser().getName();
		this.phoneNumber = owner.getUser().getPhoneNumber();
		this.email = owner.getUser().getEmail();
		this.role = owner.getUser().getRole();
		this.createdAt = owner.getUser().getCreatedAt();
		this.businessNumber = owner.getBusinessNumber();
		this.businessLicenseUrl = owner.getBusinessLicenseUrl();
		this.approvalStatus = owner.getApprovalStatus();

	}
}
