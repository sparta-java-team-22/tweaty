package com.tweaty.user.application.dto;

import com.tweaty.user.domain.model.ApprovalStatus;
import com.tweaty.user.domain.model.Owner;
import com.tweaty.user.domain.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {

	private String username;
	private String name;
	private String phoneNumber;
	private String email;

	//owner
	private String businessNumber;
	private String businessLicenseUrl;
	private ApprovalStatus approvalStatus;

	public UserInfoResponseDto(User user) {

		this.username = user.getUsername();
		this.name = user.getName();
		this.phoneNumber = user.getPhoneNumber();
		this.email = user.getEmail();

	}

	public UserInfoResponseDto(Owner owner) {

		this.username = owner.getUser().getUsername();
		this.name = owner.getUser().getName();
		this.phoneNumber = owner.getUser().getPhoneNumber();
		this.email = owner.getUser().getEmail();
		this.businessNumber = owner.getBusinessNumber();
		this.businessLicenseUrl = owner.getBusinessLicenseUrl();
		this.approvalStatus = owner.getApprovalStatus();

	}
}
