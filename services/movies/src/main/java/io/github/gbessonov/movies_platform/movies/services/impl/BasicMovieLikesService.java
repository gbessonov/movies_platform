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

    public void likeMovie(String userName, String movieId) throws IllegalArgumentException {
        validateUserName(userName);
        UUID safeMovieId = parseMovieId(movieId);
        if (!moviesRepository.existsById(safeMovieId)) {
            throw new IllegalArgumentException("Movie with ID " + movieId + " does not exist");
        }
        repository.likeMovie(userName, safeMovieId);
    }

    public void unlikeMovie(String userName, String movieId) {
        validateUserName(userName);
        UUID safeMovieId = parseMovieId(movieId);
        if (!moviesRepository.existsById(safeMovieId)) {
            throw new IllegalArgumentException("Movie with ID " + movieId + " does not exist");
        }
        repository.unlikeMovie(userName, safeMovieId);
    }

    private void validateUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("User Name cannot be null or empty");
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
