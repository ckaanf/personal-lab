package org.object.v2.reservation;

import java.time.LocalDateTime;

import org.object.v2.generic.Money;

public class Screening {
	private Movie movie;
	private int sequence;
	private LocalDateTime whenScreened;

	public Reservation reserve(Customer customer, int audienceCount) {
		Money fee = movie.calculateFee(this).times(audienceCount);
		return new Reservation(customer, this, audienceCount, fee);
	}

	public Money getFixedFee() {
		return movie.getFee();
	}

	public boolean isSequence(int sequence) {
		return this.sequence == sequence;
	}

	public LocalDateTime getStartTime() {
		return whenScreened;
	}
}
