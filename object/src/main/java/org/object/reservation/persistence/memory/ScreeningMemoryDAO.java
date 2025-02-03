package org.object.reservation.persistence.memory;

import org.object.reservation.domain.Screening;
import org.object.reservation.persistence.ScreeningDAO;

public class ScreeningMemoryDAO extends InMemoryDAO<Screening> implements ScreeningDAO {
	@Override
	public Screening selectScreening(Long id) {
		return findOne(screening -> screening.getId().equals(id));
	}
}
