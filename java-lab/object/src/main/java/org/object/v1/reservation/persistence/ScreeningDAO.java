package org.object.v1.reservation.persistence;

import org.object.v1.reservation.domain.Screening;

public interface ScreeningDAO {
	Screening selectScreening(Long screeningId);

	void insert(Screening screening);
}
