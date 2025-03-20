package org.object.v2.reservation;

import org.object.v2.generic.Money;

public class Reservation {
	private final Customer customer;
	private final Screening screening;
	private final int audienceCount;
	private final Money fee;

	public Reservation(Customer customer, Screening screening, int audienceCount, Money fee) {
		this.customer = customer;
		this.screening = screening;
		this.audienceCount = audienceCount;
		this.fee = fee;
	}
}
