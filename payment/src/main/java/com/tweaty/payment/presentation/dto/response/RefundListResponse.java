package com.tweaty.payment.presentation.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RefundListResponse {
	private List<RefundResponseDto> refundList;
	private Pagination pagination;

	@Getter
	@AllArgsConstructor
	@Builder
	public static class Pagination {
		private int page;
		private int size;
		private int totalPages;
		private Long totalElements;
	}

	public static RefundListResponse from(Page<RefundResponseDto> page) {
		return RefundListResponse.builder()
			.refundList(page.getContent())
			.pagination(Pagination.builder()
				.page(page.getNumber())
				.size(page.getSize())
				.totalPages(page.getTotalPages())
				.totalElements(page.getTotalElements())
				.build())
			.build();
	}
}


