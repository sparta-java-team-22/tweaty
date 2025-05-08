package com.tweaty.user.application.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.user.application.dto.NotificationRequestDto;
import com.tweaty.user.application.dto.OwnerStatusChangeResponseDto;
import com.tweaty.user.application.dto.OwnerCreateRequestDto;
import com.tweaty.user.application.dto.OwnerResponseDto;
import com.tweaty.user.application.dto.UserCreateRequestDto;
import com.tweaty.user.application.dto.UserDto;
import com.tweaty.user.application.dto.UserInfoResponseDto;
import com.tweaty.user.application.dto.UserListDto;
import com.tweaty.user.application.dto.UserMailDto;
import com.tweaty.user.application.dto.UserResponseDto;
import com.tweaty.user.application.dto.UserUpdateRequestDto;
import com.tweaty.user.application.dto.UserUpdateResponseDto;
import com.tweaty.user.application.dto.UserViewResponseDto;
import com.tweaty.user.domain.model.ApprovalStatus;
import com.tweaty.user.domain.model.Owner;
import com.tweaty.user.domain.model.User;
import com.tweaty.user.domain.repository.OwnerRepository;
import com.tweaty.user.domain.repository.UserRepository;
import com.tweaty.user.exception.AlreadyApprovedException;
import com.tweaty.user.exception.EmailAlreadyExistsException;
import com.tweaty.user.exception.InvalidApprovalStatusException;
import com.tweaty.user.exception.InvalidRoleException;
import com.tweaty.user.exception.PhoneAlreadyExistsException;
import com.tweaty.user.exception.UserNotFoundException;
import com.tweaty.user.exception.UsernameAlreadyExistsException;
import com.tweaty.user.infrastructure.client.NotificationServiceClient;

import domain.NotiChannel;
import domain.NotiType;
import domain.Role;
import domain.TargetType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final OwnerRepository ownerRepository;
	private final NotificationServiceClient notificationServiceClient;
	private final KafkaTemplate<String, Object> kafkaTemplate;

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
			.orElseThrow(UserNotFoundException::new);

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
	@Transactional
	public void deleteUser(UUID id) {

		User user = userFindById(id);

		user.softDelete();
	}

	//이메일 주소 가져오기
	public UserMailDto getUserMail(UUID receiverId) {

		User user = userFindById(receiverId);

		return new UserMailDto(user);
	}

	//admin
	//회원 단일 조회
	public UserViewResponseDto getUser(UUID id) {

		User user = userFindById(id);

		switch (user.getRole()) {
			case ROLE_CUSTOMER:
			case ROLE_ADMIN:
				return new UserViewResponseDto(user);

			case ROLE_OWNER:
				Owner owner = ownerFindById(id);
				return new UserViewResponseDto(owner);

			default:
				throw new UserNotFoundException();
		}
	}

	public Page<UserListDto> getUserList(String key, Role role, int page, int limit, String sortBy, String order) {

		if (limit != 10 && limit != 30 && limit != 50) {
			limit = 10;
		}

		Pageable pageable = PageRequest.of(page, limit);

		switch (role) {
			case ROLE_CUSTOMER:
			case ROLE_ADMIN:
				return userRepository.getUserList(key, role, pageable, sortBy, order).map(UserListDto::new);

			case ROLE_OWNER:
				return ownerRepository.getOwnerList(key, pageable, sortBy, order).map(UserListDto::new);

			default:
				throw new InvalidRoleException();
		}
	}

	public Page<UserListDto> getPendingOwnersList(int page, int limit, String sortBy, String order) {

		if (limit != 10 && limit != 30 && limit != 50) {
			limit = 10;
		}

		Pageable pageable = PageRequest.of(page, limit);

		return ownerRepository.getOwnersList(ApprovalStatus.PENDING, pageable, sortBy, order).map(UserListDto::new);
	}

	public Page<UserListDto> getRejectedOwnersList(int page, int limit, String sortBy, String order) {

		if (limit != 10 && limit != 30 && limit != 50) {
			limit = 10;
		}

		Pageable pageable = PageRequest.of(page, limit);

		return ownerRepository.getOwnersList(ApprovalStatus.REJECTED, pageable, sortBy, order).map(UserListDto::new);
	}

	@Transactional
	public OwnerStatusChangeResponseDto approveOwner(UUID id) {

		Owner owner = ownerFindById(id);

		if(owner.getApprovalStatus().equals(ApprovalStatus.PENDING)
			|| owner.getApprovalStatus().equals(ApprovalStatus.REJECTED)
		) {
			owner.approveOwner();

			Set<NotiChannel> channels = new HashSet<>();
			channels.add(NotiChannel.WEB);
			channels.add(NotiChannel.EMAIL);

			NotificationRequestDto requestDto = new NotificationRequestDto(
				id,
				TargetType.SIGNUP,
				channels,
				NotiType.SIGNUP_OWNER_APPROVED,
				null
			);

			kafkaTemplate.send("notification-topic", requestDto);

			// notificationServiceClient.createSignupNotification(requestDto);

			return new OwnerStatusChangeResponseDto(owner);

		} else if(owner.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
			throw new AlreadyApprovedException();

		} else {
			throw new InvalidApprovalStatusException();
		}
	}

	@Transactional
	public OwnerStatusChangeResponseDto rejectOwner(UUID id, String reason) {

		Owner owner = ownerFindById(id);

		if(owner.getApprovalStatus().equals(ApprovalStatus.PENDING)
			|| owner.getApprovalStatus().equals(ApprovalStatus.REJECTED)
		) {
			owner.rejectOwner();

			Set<NotiChannel> channels = new HashSet<>();
			channels.add(NotiChannel.WEB);
			channels.add(NotiChannel.EMAIL);

			NotificationRequestDto requestDto = new NotificationRequestDto(
				id,
				TargetType.SIGNUP,
				channels,
				NotiType.SIGNUP_OWNER_REJECTED,
				reason
			);

			kafkaTemplate.send("notification-topic", requestDto);

			// notificationServiceClient.createSignupNotification(requestDto);

			return new OwnerStatusChangeResponseDto(owner, reason);

		} else if(owner.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
			throw new AlreadyApprovedException();

		} else {
			throw new InvalidApprovalStatusException();
		}
	}

	//중복 메서드
	//유저 존재 확인
	public User userFindById(UUID id) {
		return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
	}

	//가게 주인 존재 확인
	public Owner ownerFindById(UUID id) {
		return ownerRepository.findById(id).orElseThrow(UserNotFoundException::new);
	}

	//중복 체크
	private void checkDuplicateUser(UserCreateRequestDto requestDto) {

		if (userRepository.existsByUsername(requestDto.getUsername())) {
			throw new UsernameAlreadyExistsException();
		}
		if (userRepository.existsByEmail(requestDto.getEmail())) {
			throw new EmailAlreadyExistsException();
		}
		if (userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
			throw new PhoneAlreadyExistsException();
		}
	}

}