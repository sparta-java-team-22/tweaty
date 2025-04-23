package com.tweaty.user.application.dto;

import java.util.UUID;

import com.tweaty.user.domain.model.ApprovalStatus;
import com.tweaty.user.domain.model.Owner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnerStatusChangeResponseDto {

	private UUID id;
	private String username;
	private String businessNumber;
	private ApprovalStatus approvalStatus;
	private String message;

	public OwnerStatusChangeResponseDto(Owner owner){

		this.id = owner.getUser().getId();
		this.username = owner.getUser().getUsername();
		this.businessNumber = owner.getBusinessNumber();
		this.approvalStatus = owner.getApprovalStatus();

	}

	// 거절 메시지를 설정하는 생성자
	public OwnerStatusChangeResponseDto(Owner owner, String message) {
		this(owner);
		this.message = message;
	}
}
