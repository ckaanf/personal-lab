package org.object.v1.reservation.persistence;

import java.util.List;

import org.object.v1.reservation.domain.DiscountCondition;

public interface DiscountConditionDAO {
	List<DiscountCondition> selectDiscountConditions(Long policyId);

	void insert(DiscountCondition discountCondition);
}
