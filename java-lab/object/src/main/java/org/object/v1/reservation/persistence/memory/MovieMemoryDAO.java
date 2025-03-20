package org.object.v1.reservation.persistence.memory;

import org.object.v1.reservation.domain.Movie;
import org.object.v1.reservation.persistence.MovieDAO;

public class MovieMemoryDAO extends InMemoryDAO<Movie> implements MovieDAO {
	@Override
	public Movie selectMovie(Long movieId) {
		return findOne(movie -> movie.getId().equals(movieId));
	}
}
