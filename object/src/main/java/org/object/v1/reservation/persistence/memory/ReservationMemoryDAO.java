package org.object.v1.reservation.persistence.memory;

import org.object.v1.reservation.domain.Reservation;
import org.object.v1.reservation.persistence.ReservationDAO;

public class ReservationMemoryDAO extends InMemoryDAO<Reservation> implements ReservationDAO {
	@Override
	public void insert(Reservation reservation) {
		super.insert(reservation);
	}
}
