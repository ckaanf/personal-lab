package org.object.reservation.persistence.memory;

import java.util.List;

import org.object.reservation.domain.DiscountCondition;
import org.object.reservation.persistence.DiscountConditionDAO;

public class DiscountConditionMemoryDAO extends InMemoryDAO<DiscountCondition> implements DiscountConditionDAO {
	@Override
	public List<DiscountCondition> selectDiscountConditions(Long policyId) {
		return findMany(condition -> condition.getPolicyId().equals(policyId));
	}
}
