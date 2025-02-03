package org.object.reservation.persistence.memory;

import org.object.reservation.domain.DiscountPolicy;
import org.object.reservation.persistence.DiscountPolicyDAO;

public class DiscountPolicyMemoryDAO extends InMemoryDAO<DiscountPolicy> implements DiscountPolicyDAO {
	@Override
	public DiscountPolicy selectDiscountPolicy(Long movieId) {
		return findOne(policy -> policy.getMovieId().equals(movieId));
	}
}
