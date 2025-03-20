package org.object.v1.reservation.persistence.memory;

import org.object.v1.reservation.domain.DiscountPolicy;
import org.object.v1.reservation.persistence.DiscountPolicyDAO;

public class DiscountPolicyMemoryDAO extends InMemoryDAO<DiscountPolicy> implements DiscountPolicyDAO {
	@Override
	public DiscountPolicy selectDiscountPolicy(Long movieId) {
		return findOne(policy -> policy.getMovieId().equals(movieId));
	}
}
