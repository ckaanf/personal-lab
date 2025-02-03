package org.object.reservation.persistence;

import java.util.List;

import org.object.reservation.domain.DiscountCondition;

public interface DiscountConditionDAO {
	List<DiscountCondition> selectDiscountConditions(Long policyId);

	void insert(DiscountCondition discountCondition);
}
