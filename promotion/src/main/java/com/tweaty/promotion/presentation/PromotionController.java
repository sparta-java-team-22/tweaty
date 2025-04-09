package com.tweaty.promotion.presentation;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweaty.promotion.application.PromotionService;
import com.tweaty.promotion.application.dto.PromotionCreateResponse;
import com.tweaty.promotion.application.dto.PromotionReadResponse;
import com.tweaty.promotion.presentation.request.PromotionCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotions")
public class PromotionController {
	private final PromotionService promotionService;

	@PostMapping()
	public ResponseEntity<PromotionCreateResponse> createEvent(@RequestBody PromotionCreateRequest request) {
		PromotionCreateResponse response = promotionService.createEvent(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{eventId}")
	public ResponseEntity<PromotionReadResponse> getEvent(@PathVariable UUID eventId) {
		PromotionReadResponse response = promotionService.getEvent(eventId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PatchMapping("/{eventId}/end")
	public ResponseEntity<Void> updateEventStatusToEnded(@PathVariable UUID eventId) {
		promotionService.updateEventStatusToEnded(eventId);
		return ResponseEntity.noContent().build();
	}
}
