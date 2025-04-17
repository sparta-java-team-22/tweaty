package com.tweaty.reservation.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.tweaty.reservation.domain.model.Reservation;

public interface ReservationRepository {
	void save(Reservation reservation);

	Optional<Reservation> findByIdAndIsDeletedFalse(UUID reservationId);
}
