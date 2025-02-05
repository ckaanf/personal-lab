package org.object.reservation.service;

import java.util.List;

import org.object.generic.Money;
import org.object.reservation.domain.DiscountCondition;
import org.object.reservation.domain.DiscountPolicy;
import org.object.reservation.domain.Movie;
import org.object.reservation.domain.Reservation;
import org.object.reservation.domain.Screening;
import org.object.reservation.persistence.DiscountConditionDAO;
import org.object.reservation.persistence.DiscountPolicyDAO;
import org.object.reservation.persistence.MovieDAO;
import org.object.reservation.persistence.ReservationDAO;
import org.object.reservation.persistence.ScreeningDAO;

public class ReservationService {
	private ScreeningDAO screeningDAO;
	private MovieDAO movieDAO;
	private DiscountPolicyDAO discountPolicyDAO;
	private DiscountConditionDAO discountConditionDAO;
	private ReservationDAO reservationDAO;

	public ReservationService(ScreeningDAO screeningDAO,
		MovieDAO movieDAO,
		DiscountPolicyDAO discountPolicyDAO,
		DiscountConditionDAO discountConditionDAO,
		ReservationDAO reservationDAO) {
		this.screeningDAO = screeningDAO;
		this.movieDAO = movieDAO;
		this.discountConditionDAO = discountConditionDAO;
		this.discountPolicyDAO = discountPolicyDAO;
		this.reservationDAO = reservationDAO;
	}

	public Reservation reserveScreening(Long customerId, Long screeningId, Integer audienceCount) {
		Screening screening = screeningDAO.selectScreening(screeningId);
		Movie movie = movieDAO.selectMovie(screening.getMovieId());
		DiscountPolicy policy = discountPolicyDAO.selectDiscountPolicy(movie.getId());
		List<DiscountCondition> conditions = discountConditionDAO.selectDiscountConditions(policy.getId());

		DiscountCondition condition = findDiscountCondition(screening, conditions);

		Money fee;
		if (condition != null) {
			fee = movie.getFee().minus(calculateDiscount(policy, movie));
		} else {
			fee = movie.getFee();
		}

		Reservation reservation = makeReservation(customerId, screeningId, audienceCount, fee);
		reservationDAO.insert(reservation);

		return reservation;
	}

	private DiscountCondition findDiscountCondition(Screening screening, List<DiscountCondition> conditions) {
		for(DiscountCondition condition : conditions) {
			if (condition.isPeriodCondition()) {
				if (screening.isPlayedIn(condition.getDayOfWeek(),
					condition.getInterval().getStartTime(),
					condition.getInterval().getEndTime())) {
					return condition;
				}
			} else if (condition.isSequenceCondition()) {
				if (condition.getSequence().equals(screening.getSequence())) {
					return condition;
				}
			} else if (condition.isCombinedCondition()) {
				if (screening.isPlayedIn(condition.getDayOfWeek(),
					condition.getInterval().getStartTime(),
					condition.getInterval().getEndTime()) &&
					condition.getSequence().equals(screening.getSequence())) {
					return condition;
				}
			}
		}

		return null;
	}

	private Money calculateDiscount(DiscountPolicy policy, Movie movie) {
		if (policy.isAmountPolicy()) {
			return policy.getAmount();
		} else if (policy.isPercentPolicy()) {
			return movie.getFee().times(policy.getPercent());
		}

		return Money.ZERO;
	}

	private Reservation makeReservation(Long customerId, Long screeningId, Integer audienceCount, Money fee) {
		return new Reservation(customerId, screeningId, audienceCount, fee.times(audienceCount));
	}
}
