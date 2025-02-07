package org.object.v2.reservation;

import java.util.List;

import org.object.v2.generic.Money;

public abstract class DiscountPolicy {
	private List<DiscountCondition> conditions;

	public Money calculateDiscount(Screening screening) {
		for(DiscountCondition each : conditions) {
			if(each.isSatisfiedBy(screening)) {
				return getDiscountAmount(screening);
			}
		}
		return Money.ZERO;
	}

	protected abstract Money getDiscountAmount(Screening screening);
}
