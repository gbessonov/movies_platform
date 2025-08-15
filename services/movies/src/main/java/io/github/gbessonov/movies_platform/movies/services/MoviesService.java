package io.github.gbessonov.movies_platform.movies.services;

import io.github.gbessonov.movies_platform.movies.model.CreateMovie;
import io.github.gbessonov.movies_platform.movies.model.Movie;
import io.github.gbessonov.movies_platform.movies.model.UpdateMovie;

import java.util.List;
import java.util.Optional;

public interface MoviesService {
    List<Movie> getMovies();
    Optional<Movie> getMovieById(String id);
    Movie createMovie(CreateMovie movie);
    Movie updateMovie(String id, UpdateMovie movie);
    void deleteMovie(String id);
}
