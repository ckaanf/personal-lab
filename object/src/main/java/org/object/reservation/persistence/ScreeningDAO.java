package org.object.reservation.persistence;

import org.object.reservation.domain.Screening;

public interface ScreeningDAO {
	Screening selectScreening(Long screeningId);

	void insert(Screening screening);
}
