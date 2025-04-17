package com.tweaty.user.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserListReponseDto {

	private List<UserListDto> users;
	private PageInfo pageInfo;

}
