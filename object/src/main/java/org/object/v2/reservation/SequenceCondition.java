package org.object.v2.reservation;

public class SequenceCondition implements DiscountCondition {
	private int sequence;

	@Override
	public boolean isSatisfiedBy(Screening screening) {
		return screening.isSequence(sequence);
	}
}
