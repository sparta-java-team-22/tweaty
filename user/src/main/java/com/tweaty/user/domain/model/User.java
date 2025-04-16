package com.tweaty.user.domain.model;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.Where;

import base.BaseEntity;
import domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
@Where(clause = "is_deleted = false")
public class User extends BaseEntity {

	@Id
	@UuidGenerator
	private UUID id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String phoneNumber;

	@Column(nullable = false, unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	public static User create(String username, String password, String name, String email, String phoneNumber,
		Role role) {

		User user = new User();
		user.username = username;
		user.password = password;
		user.name = name;
		user.email = email;
		user.phoneNumber = phoneNumber;
		user.role = role;

		return user;
	}

	public void updateInfo(String phoneNumber, String email) {
		if (phoneNumber != null) {
			this.phoneNumber = phoneNumber;
		}
		if (email != null) {
			this.email = email;
		}
	}

	public void updatePassword(String encodedPassword) {
		this.password = encodedPassword;
	}
}