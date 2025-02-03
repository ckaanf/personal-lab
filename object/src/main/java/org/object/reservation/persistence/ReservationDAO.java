package org.object.reservation.persistence;

import org.object.reservation.domain.Reservation;

public interface ReservationDAO {
	void insert(Reservation reservation);
}
