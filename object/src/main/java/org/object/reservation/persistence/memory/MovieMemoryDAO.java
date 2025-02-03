package org.object.reservation.persistence.memory;

import org.object.reservation.domain.Movie;
import org.object.reservation.persistence.MovieDAO;

public class MovieMemoryDAO extends InMemoryDAO<Movie> implements MovieDAO {
	@Override
	public Movie selectMovie(Long movieId) {
		return findOne(movie -> movie.getId().equals(movieId));
	}
}
