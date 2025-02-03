package org.object.reservation.persistence.memory;

import org.object.reservation.domain.Reservation;
import org.object.reservation.persistence.ReservationDAO;

public class ReservationMemoryDAO extends InMemoryDAO<Reservation> implements ReservationDAO {
	@Override
	public void insert(Reservation reservation) {
		super.insert(reservation);
	}
}
