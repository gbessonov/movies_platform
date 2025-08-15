package io.github.gbessonov.movies_platform.movies.services.impl;

import io.github.gbessonov.movies_platform.movies.entities.DbMovie;
import io.github.gbessonov.movies_platform.movies.model.CreateMovie;
import io.github.gbessonov.movies_platform.movies.model.Movie;
import io.github.gbessonov.movies_platform.movies.model.UpdateMovie;
import io.github.gbessonov.movies_platform.movies.repositories.MoviesRepository;
import io.github.gbessonov.movies_platform.movies.services.MoviesService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class BasicMoviesService implements MoviesService {

    private final MoviesRepository moviesRepository;

    @Autowired
    public BasicMoviesService(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @Override
    public List<Movie> getMovies() {
        return moviesRepository.get().stream()
                .map(BasicMoviesService::mapToMovie)
                .toList();
    }

    @Override
    public Optional<Movie> getMovieById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }

        try {
            UUID safeId = UUID.fromString(id);
            DbMovie entity = moviesRepository.get(safeId).orElse(null);
            return Optional.ofNullable(entity)
                    .map(BasicMoviesService::mapToMovie);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public Movie createMovie(CreateMovie movie) throws IllegalArgumentException {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null");
        }
        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            throw new IllegalArgumentException("Movie title cannot be null or empty");
        }
        DbMovie dbMovie = new DbMovie();
        dbMovie.title = movie.getTitle();
        DbMovie savedMovie = moviesRepository.save(dbMovie);
        return mapToMovie(savedMovie);
    }

    @Override
    public Movie updateMovie(String id, UpdateMovie movie) throws IllegalArgumentException {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null");
        }
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Movie or Movie ID cannot be null or empty");
        }
        try {
            UUID safeId = UUID.fromString(id);
            DbMovie dbMovie = moviesRepository
                    .get(safeId)
                    .orElseThrow(() -> new IllegalArgumentException("Movie with ID '" + safeId + "' not found"));
            dbMovie.title = movie.getTitle();
            DbMovie savedMovie = moviesRepository.save(dbMovie);
            return mapToMovie(savedMovie);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Movie ID format: " + id, e);
        }
    }

    @Override
    public void deleteMovie(String id) throws IllegalArgumentException {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Movie ID cannot be null or empty");
        }

        try {
            UUID safeId = UUID.fromString(id);
            moviesRepository.deleteById(safeId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Movie ID format: " + id, e);
        }
    }

    private static Movie mapToMovie(DbMovie dbMovie) {
        if (dbMovie == null) {
            return null;
        }
        return new Movie()
                .id(dbMovie.id)
                .title(dbMovie.title)
                .likes(dbMovie.numberOfLikes);
    }
}
