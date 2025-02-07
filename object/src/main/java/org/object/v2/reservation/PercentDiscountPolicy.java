package org.object.v2.reservation;

import org.object.v2.generic.Money;

public class PercentDiscountPolicy extends DiscountPolicy {
	private double percent;

	@Override
	protected Money getDiscountAmount(Screening screening) {
		return screening.getFixedFee().times(percent);
	}
}
