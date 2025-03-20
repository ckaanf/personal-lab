package org.object.v2.reservation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.object.v2.generic.Money;

class MovieTest {

	@DisplayName("비율할인정책_계산하기")
	@Test
	public void calculatePolicyPercent() {
		Movie movie = new Movie(
			Money.wons(10000),
			new PercentDiscountPolicy(0.1,
				new SequenceCondition(1),
				new SequenceCondition(3),
				new PeriodCondition(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 59))));

		Screening screening = new Screening(movie, 1, LocalDateTime.of(2024, 12, 11, 18, 0));
		Money fee = movie.calculateFee(screening);

		assertEquals(Money.wons(9000), fee);
	}

	@DisplayName("정액할인정책_계산하기")
	@Test
	public void calculatePolicyAmount() {
		Movie movie = new Movie(
			Money.wons(10000),
			new AmountDiscountPolicy(
				Money.wons(1000),
				new SequenceCondition(1),
				new PeriodCondition(DayOfWeek.WEDNESDAY, LocalTime.of(18, 0), LocalTime.of(21, 10)),
				new PeriodCondition(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 59))));

		Screening screening = new Screening(movie, 1, LocalDateTime.of(2024, 12, 11, 18, 0));
		Money fee = movie.calculateFee(screening);

		assertEquals(Money.wons(9000), fee);
	}

	@DisplayName("계산하기")
	@Test
	public void calculatePolicy() {
		Movie movie = new Movie(
			Money.wons(10000),
			new NonDiscountPolicy());

		Screening screening = new Screening(movie, 1, LocalDateTime.of(2024, 12, 11, 18, 0));
		Money fee = movie.calculateFee(screening);

		assertEquals(Money.wons(10000), fee);
	}

}