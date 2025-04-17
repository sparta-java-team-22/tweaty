package com.tweaty.reservation.domain.repository;

import com.tweaty.reservation.domain.model.Reservation;

public interface ReservationRepository {
	void save(Reservation reservation);
}
