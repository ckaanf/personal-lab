package org.object.v1.reservation.persistence;

import org.object.v1.reservation.domain.Reservation;

public interface ReservationDAO {
	void insert(Reservation reservation);
}
