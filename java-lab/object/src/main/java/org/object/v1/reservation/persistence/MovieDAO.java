package org.object.v1.reservation.persistence;

import org.object.v1.reservation.domain.Movie;

public interface MovieDAO {
	Movie selectMovie(Long movieId);

	void insert(Movie movie);
}
