package org.object;

import static java.time.DayOfWeek.*;
import static org.object.v1.reservation.domain.DiscountCondition.ConditionType.*;
import static org.object.v1.reservation.domain.DiscountPolicy.PolicyType.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.object.v1.generic.Money;
import org.object.v1.reservation.domain.DiscountCondition;
import org.object.v1.reservation.domain.DiscountPolicy;
import org.object.v1.reservation.domain.Movie;
import org.object.v1.reservation.domain.Reservation;
import org.object.v1.reservation.domain.Screening;
import org.object.v1.reservation.persistence.DiscountConditionDAO;
import org.object.v1.reservation.persistence.DiscountPolicyDAO;
import org.object.v1.reservation.persistence.MovieDAO;
import org.object.v1.reservation.persistence.ReservationDAO;
import org.object.v1.reservation.persistence.ScreeningDAO;
import org.object.v1.reservation.persistence.memory.DiscountConditionMemoryDAO;
import org.object.v1.reservation.persistence.memory.DiscountPolicyMemoryDAO;
import org.object.v1.reservation.persistence.memory.MovieMemoryDAO;
import org.object.v1.reservation.persistence.memory.ReservationMemoryDAO;
import org.object.v1.reservation.persistence.memory.ScreeningMemoryDAO;
import org.object.v1.reservation.service.ReservationService;

public class Main {
	private ScreeningDAO screeningDAO = new ScreeningMemoryDAO();
	private MovieDAO movieDAO = new MovieMemoryDAO();
	private DiscountPolicyDAO discountPolicyDAO = new DiscountPolicyMemoryDAO();
	private DiscountConditionDAO discountConditionDAO = new DiscountConditionMemoryDAO();
	private ReservationDAO reservationDAO = new ReservationMemoryDAO();

	ReservationService reservationService = new ReservationService(
		screeningDAO,
		movieDAO,
		discountPolicyDAO,
		discountConditionDAO,
		reservationDAO);

	private Screening initializeData() {
		Movie movie = new Movie("한산", 150, Money.wons(10000));
		movieDAO.insert(movie);

		DiscountPolicy discountPolicy = new DiscountPolicy(movie.getId(), AMOUNT_POLICY, Money.wons(1000), null);
		discountPolicyDAO.insert(discountPolicy);

		discountConditionDAO.insert(
			new DiscountCondition(discountPolicy.getId(), SEQUENCE_CONDITION, null, null, null, 1));
		discountConditionDAO.insert(
			new DiscountCondition(discountPolicy.getId(), SEQUENCE_CONDITION, null, null, null, 10));
		discountConditionDAO.insert(
			new DiscountCondition(discountPolicy.getId(), PERIOD_CONDITION, MONDAY, LocalTime.of(10, 0),
				LocalTime.of(12, 0), null));
		discountConditionDAO.insert(
			new DiscountCondition(discountPolicy.getId(), PERIOD_CONDITION, WEDNESDAY, LocalTime.of(18, 0),
				LocalTime.of(21, 0), null));

		Screening screening = new Screening(movie.getId(), 7, LocalDateTime.of(2024, 12, 11, 18, 0));
		screeningDAO.insert(screening);

		return screening;
	}

	public void run() {
		Screening screening = initializeData();

		Reservation reservation = reservationService.reserveScreening(1L, screening.getId(), 2);

		System.out.printf("관객수 : %d, 요금: %s%n", reservation.getAudienceCount(), reservation.getFee());
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
