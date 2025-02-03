package org.object.reservation.persistence;

import org.object.reservation.domain.Movie;

public interface MovieDAO {
	Movie selectMovie(Long movieId);

	void insert(Movie movie);
}
