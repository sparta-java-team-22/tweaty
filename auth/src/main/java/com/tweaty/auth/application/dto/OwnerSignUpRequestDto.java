package com.tweaty.auth.application.dto;

import domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OwnerSignUpRequestDto {

	@NotBlank(message = "아이디는 필수입니다.")
	@Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하여야 합니다.")
	private String username;

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[~`!@#$%^&*()\\-_=+\\[\\]{};:'\\\",.<>/?])[A-Za-z0-9~`!@#$%^&*()\\-_=+\\[\\]{};:'\\\",.<>/?]{8,20}$", message = "비밀번호는 8~20자, 영문자+숫자+특수문자를 포함해야 합니다.")
	private String password;

	@NotBlank(message = "이름은 필수입니다.")
	private String name;

	@NotBlank(message = "핸드폰 번호는 필수입니다.")
	@Pattern(regexp = "^01[016789][0-9]{7,8}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
	private String phoneNumber;

	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "유효한 이메일 형식이 아닙니다.")
	private String email;

	private final Role role = Role.ROLE_OWNER;

	@NotBlank(message = "사업자 등록번호는 필수입니다.")
	private String businessNumber;

}
