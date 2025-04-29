package com.tweaty.reservation.application;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweaty.reservation.application.dto.ReservationScheduleResponseDto;
import com.tweaty.reservation.domain.model.ReservationSchedule;
import com.tweaty.reservation.domain.repository.ReservationScheduleRepository;
import com.tweaty.reservation.presentation.request.ReservationScheduleRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationScheduleService {

	private final ReservationScheduleRepository reservationScheduleRepository;
	private final ReservationCacheService cacheService;

	private static final long CACHE_TTL = 10; // Cache Time-To-Live in minutes

	public void createReservation(ReservationScheduleRequestDto requestDto) {
		ReservationSchedule reservationSchedule = reservationScheduleRepository.save(
			new ReservationSchedule(requestDto));
		// Cache the newly created reservation schedule
		cacheService.cacheReservation("schedule:" + reservationSchedule.getId(), reservationSchedule, CACHE_TTL);
	}

	@Transactional(readOnly = true)
	public List<ReservationScheduleResponseDto> getReservation(UUID storeId) {
		// Cache is not applied here as this is a list retrieval
		List<ReservationSchedule> reservationScheduleList = reservationScheduleRepository.findByStoreId(storeId);
		if (reservationScheduleList.isEmpty()) {
			throw new IllegalArgumentException("예약 일정이 없습니다.");
		}
		return ReservationScheduleResponseDto.fromList(reservationScheduleList);
	}

	@Transactional(readOnly = true)
	public ReservationScheduleResponseDto getReservationById(UUID reservationId) {
		String cacheKey = "schedule:" + reservationId;

		// Check if the data is available in Redis cache
		ReservationSchedule cachedSchedule = (ReservationSchedule)cacheService.getFromCache(cacheKey);
		if (cachedSchedule != null) {
			return ReservationScheduleResponseDto.from(cachedSchedule);
		}

		// Fallback to database query if cache miss
		ReservationSchedule reservationSchedule = reservationScheduleRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

		// Cache the result
		cacheService.cacheReservation(cacheKey, reservationSchedule, CACHE_TTL);

		return ReservationScheduleResponseDto.from(reservationSchedule);
	}

	public void updateReservation(UUID reservationId, ReservationScheduleRequestDto requestDto) {
		ReservationSchedule reservationSchedule = reservationScheduleRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
		reservationSchedule.update(requestDto);
		reservationScheduleRepository.save(reservationSchedule);

		// Update the cache
		cacheService.cacheReservation("schedule:" + reservationId, reservationSchedule, CACHE_TTL);
	}

	public void deleteReservation(UUID reservationId) {
		ReservationSchedule reservationSchedule = reservationScheduleRepository.findByIdAndIsDeletedFalse(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
		reservationSchedule.softDelete();
		reservationScheduleRepository.save(reservationSchedule);

		// Remove the cache entry
		cacheService.removeFromCache("schedule:" + reservationId);
	}
}