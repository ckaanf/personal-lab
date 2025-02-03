package org.object.reservation.persistence;

import org.object.reservation.domain.DiscountPolicy;

public interface DiscountPolicyDAO {
	DiscountPolicy selectDiscountPolicy(Long movieId);

	void insert(DiscountPolicy discountPolicy);
}
