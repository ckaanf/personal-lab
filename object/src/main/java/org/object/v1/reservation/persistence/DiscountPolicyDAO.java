package org.object.v1.reservation.persistence;

import org.object.v1.reservation.domain.DiscountPolicy;

public interface DiscountPolicyDAO {
	DiscountPolicy selectDiscountPolicy(Long movieId);

	void insert(DiscountPolicy discountPolicy);
}
