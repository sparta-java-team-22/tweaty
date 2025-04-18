package com.tweaty.user.domain.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_owner")
public class Owner {

	@Id
	private UUID id;

	@OneToOne
	@MapsId
	@JoinColumn(name = "id")
	private User user;

	@Column(nullable = false, unique = true)
	private String businessNumber;

	@Column(nullable = false)
	private String businessLicenseUrl;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ApprovalStatus approvalStatus;

	public static Owner create(User user, String businessNumber, String businessLicenseUrl) {

		Owner owner = new Owner();
		owner.user = user;
		owner.businessNumber = businessNumber;
		owner.businessLicenseUrl = businessLicenseUrl;
		owner.approvalStatus = ApprovalStatus.PENDING;

		return owner;
	}
}

