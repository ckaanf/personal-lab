package org.object.v2.reservation;

public interface DiscountCondition {
	boolean isSatisfiedBy(Screening screening);
}
