package io.github.gbessonov.movies_platform.movies.services.impl;

import io.github.gbessonov.movies_platform.movies.repositories.MovieLikesRepository;
import io.github.gbessonov.movies_platform.movies.repositories.MoviesRepository;
import io.github.gbessonov.movies_platform.movies.services.MovieLikesService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Transactional
public class BasicMovieLikesService implements MovieLikesService {
    private final MovieLikesRepository repository;
    private final MoviesRepository moviesRepository;

    public BasicMovieLikesService(
            MovieLikesRepository repository,
            MoviesRepository moviesRepository
    ) {
        this.repository = repository;
        this.moviesRepository = moviesRepository;
    }

    public void likeMovie(String userId, String movieId) throws IllegalArgumentException {
        UUID safeUserId = parseUserId(userId);
        UUID safeMovieId = parseMovieId(movieId);
        if (!moviesRepository.existsById(safeMovieId)) {
            throw new IllegalArgumentException("Movie with ID " + movieId + " does not exist");
        }
        repository.likeMovie(safeUserId, safeMovieId);
    }

    public void unlikeMovie(String userId, String movieId) {
        UUID safeUserId = parseUserId(userId);
        UUID safeMovieId = parseMovieId(movieId);
        if (!moviesRepository.existsById(safeMovieId)) {
            throw new IllegalArgumentException("Movie with ID " + movieId + " does not exist");
        }
        repository.unlikeMovie(safeUserId, safeMovieId);
    }

    private UUID parseUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid User ID format", e);
        }
    }

    private UUID parseMovieId(String movieId) {
        if (movieId == null || movieId.isEmpty()) {
            throw new IllegalArgumentException("Movie ID cannot be null or empty");
        }
        try {
            return UUID.fromString(movieId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Movie ID format", e);
        }
    }
}
