package org.object.generic;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public class Money {
	public static final Money ZERO = Money.wons(0);

	private final BigDecimal amount;

	public static Money wons(long amount) {
		return new Money(BigDecimal.valueOf(amount));
	}

	public static Money wons(double amount) {
		return new Money(BigDecimal.valueOf(amount));
	}

	public static <T> Money sum(Collection<T> bags, Function<T, Money> monetary) {
		return bags.stream().map(bag -> monetary.apply(bag)).reduce(Money.ZERO, Money::plus);
	}

	Money(BigDecimal amount) {
		this.amount = amount;
	}

	public Money plus(Money amount) {
		return new Money(this.amount.add(amount.amount));
	}

	public Money minus(Money amount) {
		return new Money(this.amount.subtract(amount.amount));
	}

	public Money times(double percent) {
		return new Money(this.amount.multiply(BigDecimal.valueOf(percent)));
	}

	public Money divide(double divisor) {
		return new Money(amount.divide(BigDecimal.valueOf(divisor)));
	}

	public boolean isLessThan(Money other) {
		return amount.compareTo(other.amount) < 0;
	}

	public boolean isGreaterThanOrEqual(Money other) {
		return amount.compareTo(other.amount) >= 0;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Long longValue() {
		return amount.longValue();
	}

	public Double doubleValue() {
		return amount.doubleValue();
	}

	public String toString() {
		return amount.toString() + "원";
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other == null || getClass() != other.getClass()) {
			return false;
		}

		Money money = (Money)other;
		return Objects.equals(amount.doubleValue(), money.amount.doubleValue());
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount.doubleValue());
	}
}
