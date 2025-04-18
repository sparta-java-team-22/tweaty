package com.tweaty.reservation.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tweaty.reservation.domain.model.Reservation;

public interface ReservationRepository {
	Reservation save(Reservation reservation);

	Optional<Reservation> findByIdAndIsDeletedFalse(UUID reservationId);

	List<Reservation> findByUserId(UUID userId);
}
