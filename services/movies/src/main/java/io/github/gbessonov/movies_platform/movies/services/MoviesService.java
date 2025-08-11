package io.github.gbessonov.movies_platform.movies.services;

import io.github.gbessonov.movies_platform.movies.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MoviesService {
    List<Movie> getMovies();
    Optional<Movie> getMovieById(String id);
}
