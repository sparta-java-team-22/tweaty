package com.tweaty.reservation.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tweaty.reservation.domain.model.ReservationSchedule;

public interface ReservationScheduleRepository {
	void save(ReservationSchedule reservationSchedule);

	List<ReservationSchedule> findAll();

	Optional<ReservationSchedule> findByIdAndIsDeletedFalse(UUID reservationId);
}
