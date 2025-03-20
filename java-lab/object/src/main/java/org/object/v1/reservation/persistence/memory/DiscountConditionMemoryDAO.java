package org.object.v1.reservation.persistence.memory;

import java.util.List;

import org.object.v1.reservation.domain.DiscountCondition;
import org.object.v1.reservation.persistence.DiscountConditionDAO;

public class DiscountConditionMemoryDAO extends InMemoryDAO<DiscountCondition> implements DiscountConditionDAO {
	@Override
	public List<DiscountCondition> selectDiscountConditions(Long policyId) {
		return findMany(condition -> condition.getPolicyId().equals(policyId));
	}
}
