package org.object.v2.reservation;

import org.object.v2.generic.Money;

public class NonDiscountPolicy extends DiscountPolicy {

	public NonDiscountPolicy(DiscountCondition... conditions) {
		super(conditions);
	}

	@Override
	protected Money getDiscountAmount(Screening screening) {
		return Money.ZERO;
	}
}
