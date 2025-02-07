package org.object.v2.reservation;

import org.object.v2.generic.Money;

public class AmountDiscountPolicy extends DiscountPolicy {
	private Money discountAmount;

	@Override
	protected Money getDiscountAmount(Screening screening) {
		return discountAmount;
	}
}
