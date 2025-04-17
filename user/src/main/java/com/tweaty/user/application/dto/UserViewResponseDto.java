package com.tweaty.user.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tweaty.user.domain.model.ApprovalStatus;
import com.tweaty.user.domain.model.Owner;
import com.tweaty.user.domain.model.User;

import domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserViewResponseDto {

	private UUID id;
	private String username;
	private String name;
	private String phoneNumber;
	private String email;
	private Role role;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private boolean isDeleted;

	//owner
	private String businessNumber;
	private String businessLicenseUrl;
	private ApprovalStatus approvalStatus;

	public UserViewResponseDto(User user) {

		this.id = user.getId();
		this.username = user.getUsername();
		this.name = user.getName();
		this.phoneNumber = user.getPhoneNumber();
		this.email = user.getEmail();
		this.role = user.getRole();
		this.createdAt = user.getCreatedAt();
		this.modifiedAt = user.getModifiedAt();
		this.isDeleted = user.getIsDeleted();

	}

	public UserViewResponseDto(Owner owner) {

		this.id = owner.getId();
		this.username = owner.getUser().getUsername();
		this.name = owner.getUser().getName();
		this.phoneNumber = owner.getUser().getPhoneNumber();
		this.email = owner.getUser().getEmail();
		this.role = owner.getUser().getRole();
		this.createdAt = owner.getUser().getCreatedAt();
		this.modifiedAt = owner.getUser().getModifiedAt();
		this.isDeleted = owner.getUser().getIsDeleted();
		this.businessNumber = owner.getBusinessNumber();
		this.businessLicenseUrl = owner.getBusinessLicenseUrl();
		this.approvalStatus = owner.getApprovalStatus();

	}
}