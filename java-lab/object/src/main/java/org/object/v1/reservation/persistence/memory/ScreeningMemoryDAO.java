package org.object.v1.reservation.persistence.memory;

import org.object.v1.reservation.domain.Screening;
import org.object.v1.reservation.persistence.ScreeningDAO;

public class ScreeningMemoryDAO extends InMemoryDAO<Screening> implements ScreeningDAO {
	@Override
	public Screening selectScreening(Long id) {
		return findOne(screening -> screening.getId().equals(id));
	}
}
