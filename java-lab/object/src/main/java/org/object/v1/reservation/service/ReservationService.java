package org.object.v1.reservation.service;

import java.util.List;

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

		boolean found = policy.findDiscountCondition(screening);

		Money fee;
		if (found) {
			fee = movie.getFee().minus(policy.calculateDiscount(movie));
		} else {
			fee = movie.getFee();
		}

		Reservation reservation = makeReservation(customerId, screeningId, audienceCount, fee);
		reservationDAO.insert(reservation);

		return reservation;
	}

	private Reservation makeReservation(Long customerId, Long screeningId, Integer audienceCount, Money fee) {
		return new Reservation(customerId, screeningId, audienceCount, fee.times(audienceCount));
	}
}
