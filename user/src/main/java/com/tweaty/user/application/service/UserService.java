package com.tweaty.user.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.user.application.dto.OwnerCreateRequestDto;
import com.tweaty.user.application.dto.OwnerResponseDto;
import com.tweaty.user.application.dto.UserCreateRequestDto;
import com.tweaty.user.application.dto.UserDto;
import com.tweaty.user.application.dto.UserInfoResponseDto;
import com.tweaty.user.application.dto.UserListDto;
import com.tweaty.user.application.dto.UserResponseDto;
import com.tweaty.user.application.dto.UserUpdateRequestDto;
import com.tweaty.user.application.dto.UserUpdateResponseDto;
import com.tweaty.user.application.dto.UserViewResponseDto;
import com.tweaty.user.domain.model.Owner;
import com.tweaty.user.domain.model.User;
import com.tweaty.user.domain.repository.OwnerRepository;
import com.tweaty.user.domain.repository.UserRepository;

import domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final OwnerRepository ownerRepository;

	@Transactional
	public UserResponseDto userSignUp(UserCreateRequestDto requestDto) {

		checkDuplicateUser(requestDto);

		User user = User.create(requestDto.getUsername(), requestDto.getPassword(), requestDto.getName(),
			requestDto.getEmail(), requestDto.getPhoneNumber(), requestDto.getRole());
		userRepository.save(user);

		return new UserResponseDto(user);
	}

	@Transactional
	public OwnerResponseDto ownerSignUp(OwnerCreateRequestDto requestDto) {

		checkDuplicateUser(requestDto);

		User user = User.create(requestDto.getUsername(), requestDto.getPassword(), requestDto.getName(),
			requestDto.getEmail(), requestDto.getPhoneNumber(), requestDto.getRole());
		userRepository.save(user);

		Owner owner = Owner.create(user, requestDto.getBusinessNumber(), requestDto.getBusinessLicenseUrl());
		ownerRepository.save(owner);

		return new OwnerResponseDto(user.getId(), user.getUsername(), user.getRole(),
			owner.getApprovalStatus().toString());
	}

	//로그인
	public UserDto getUserByUsername(String username) {

		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다"));

		return new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
	}

	//정보 조회 - 일반 사용자
	public UserInfoResponseDto getCustomerInfo(UUID id) {

		User user = userFindById(id);

		return new UserInfoResponseDto(user);
	}

	//정보 조회 - 가게 주인
	public UserInfoResponseDto getOwnerInfo(UUID id) {

		Owner owner = ownerFindById(id);

		log.debug("owner의 userId: {}", owner.getId());

		return new UserInfoResponseDto(owner);
	}

	//회원 정보 수정
	@Transactional
	public UserUpdateResponseDto updateUserInfo(UUID id, UserUpdateRequestDto requestDto) {

		User user = userFindById(id);

		user.updateInfo(requestDto.getPhoneNumber(), requestDto.getEmail());

		return new UserUpdateResponseDto(user);
	}

	//비밀번호 수정
	@Transactional
	public void updatePassword(UUID id, String encodedPassword) {

		User user = userFindById(id);

		user.updatePassword(encodedPassword);
	}

	//회원 탈퇴
	public void deleteUser(UUID id) {

		User user = userFindById(id);

		user.softDelete();
	}

	//admin
	//회원 단일 조회
	public UserViewResponseDto getUser(UUID id) {

		User user = userFindById(id);

		switch (user.getRole()) {
			case ROLE_CUSTOMER:
				return new UserViewResponseDto(user);

			case ROLE_ADMIN:
				Owner owner = ownerFindById(id);
				return new UserViewResponseDto(owner);

			default:
				throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
		}
	}

	public Page<UserListDto> getUserList(String key, Role role, int page, int limit, String sortBy, String order) {

		if (limit != 10 && limit != 30 && limit != 50) {
			limit = 10;
		}

		Pageable pageable = PageRequest.of(page, limit);

		switch (role) {
			case ROLE_CUSTOMER:
				return userRepository.getUserList(key, pageable, sortBy, order).map(UserListDto::new);

			case ROLE_OWNER:
				return ownerRepository.getOwnerList(key, pageable, sortBy, order).map(UserListDto::new);

			default:
				throw new IllegalArgumentException("잘못된 권한입니다.");
		}
	}

	public Page<UserListDto> getPendingOwnersList(int page, int limit, String sortBy, String order) {

		if (limit != 10 && limit != 30 && limit != 50) {
			limit = 10;
		}

		Pageable pageable = PageRequest.of(page, limit);

		return ownerRepository.getPendingOwnersList(pageable, sortBy, order).map(UserListDto::new);
	}

	//중복 메서드
	//유저 존재 확인
	public User userFindById(UUID id) {
		return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다"));
	}

	//가게 주인 존재 확인
	public Owner ownerFindById(UUID id) {
		return ownerRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
	}

	//중복 체크
	private void checkDuplicateUser(UserCreateRequestDto requestDto) {

		//오류처리
		if (userRepository.existsByUsername(requestDto.getUsername())) {
			throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
		}
		if (userRepository.existsByEmail(requestDto.getEmail())) {
			throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
		}
		if (userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
			throw new IllegalArgumentException("이미 사용 중인 전화번호입니다.");
		}
	}
}