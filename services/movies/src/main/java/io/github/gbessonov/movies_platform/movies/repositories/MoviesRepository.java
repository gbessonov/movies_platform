package io.github.gbessonov.movies_platform.movies.repositories;

import io.github.gbessonov.movies_platform.movies.entities.DbMovie;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MoviesRepository {
    List<DbMovie> getMovies();
    Optional<DbMovie> getMovie(UUID id);
}
