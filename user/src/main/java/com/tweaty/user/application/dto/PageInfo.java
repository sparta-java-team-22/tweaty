package com.tweaty.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PageInfo {

	private int page;
	private int size;
	private int totalPages;
	private long totalElements;
	private boolean hasNext;
	private boolean hasPrevious;

}
