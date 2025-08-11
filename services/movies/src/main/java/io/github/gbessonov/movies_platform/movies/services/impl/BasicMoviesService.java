package io.github.gbessonov.movies_platform.movies.services.impl;

import io.github.gbessonov.movies_platform.movies.entities.DbMovie;
import io.github.gbessonov.movies_platform.movies.model.Movie;
import io.github.gbessonov.movies_platform.movies.repositories.MoviesRepository;
import io.github.gbessonov.movies_platform.movies.services.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BasicMoviesService implements MoviesService {

    private final MoviesRepository moviesRepository;

    @Autowired
    public BasicMoviesService(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @Override
    public List<Movie> getMovies() {
        return moviesRepository.getMovies().stream()
                .map(e -> new Movie().id(e.id).title(e.title))
                .toList();
    }

    @Override
    public Optional<Movie> getMovieById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }

        try {
            UUID safeId = UUID.fromString(id);
            DbMovie entity = moviesRepository.getMovie(safeId)
                    .orElse(null);
            return Optional.ofNullable(entity)
                    .map(e -> new Movie().id(e.id).title(e.title));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
