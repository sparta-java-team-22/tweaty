package com.tweaty.reservation.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tweaty.reservation.domain.model.Reservation;

public interface ReservationRepository {
	void save(Reservation reservation);

	List<Reservation> findAll();

	Optional<Reservation> findByIdAndIsDeletedFalse(UUID reservationId);
}
