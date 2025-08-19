package io.github.gbessonov.movies_platform.movies.controllers;

import io.github.gbessonov.movies_platform.movies.api.MoviesApi;
import io.github.gbessonov.movies_platform.movies.model.*;
import io.github.gbessonov.movies_platform.movies.services.MoviesService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Optional;

@RestController
public class MoviesController implements MoviesApi {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    private final MoviesService moviesService;

    @Autowired
    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @Override
    @RateLimiter(name = "moviesRateLimiter", fallbackMethod = "fallbackGetMovieById")
    public ResponseEntity<MovieResponse> getMovieById(String id) {
        logger.atInfo().log("Received request to get movie by id: {}", id);

        Optional<Movie> movie = moviesService.getMovieById(id);

        if (movie.isEmpty()) {
            logger.atWarn().log("Movie with id '{}' not found", id);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Movie with id '" + id + "' not found");
            errorResponse.setError("Not Found");
            errorResponse.setTimestamp(OffsetDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        logger.atInfo().log("Movie with id '{}' found: {}", id, movie.get());
        return new ResponseEntity<>(movie.get(), org.springframework.http.HttpStatus.OK);
    }

    @Override
    @RateLimiter(name = "moviesRateLimiter", fallbackMethod = "fallbackGetMovies")
    public ResponseEntity<MoviesResponse> getMovies() {
        //TODO: decide what to do with trailing slashes
        logger.atInfo().log("Received request to get all movies");
        MoviesListResponse resp = MoviesListResponse.fromList(moviesService.getMovies());
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Override
    @RateLimiter(name = "moviesRateLimiter", fallbackMethod = "fallbackGetTopMovies")
    public ResponseEntity<MoviesResponse> getTopMovies(Integer number) {
        //TODO: decide what to do with trailing slashes
        logger.atInfo().log("Received request to get top {} movies", number);
        MoviesListResponse resp = MoviesListResponse.fromList(moviesService.getTopMovies(number));
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Override
    @RateLimiter(name = "moviesRateLimiter", fallbackMethod = "fallbackCreateMovie")
    public ResponseEntity<MovieResponse> createMovie(CreateMovie createMovie) {
        try {
            logger.atInfo().log("Received request to create movie: {}", createMovie);
            Movie m = moviesService.createMovie(createMovie);
            logger.atInfo().log("Movie with id '{}' created: {}", m.getId(), m);
            return new ResponseEntity<>(m, HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.atError().withThrowable(ex).log("Error creating movie: {}", createMovie);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Error creating movie " + createMovie.getTitle());
            errorResponse.setError("Bad Request");
            errorResponse.setTimestamp(OffsetDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @RateLimiter(name = "moviesRateLimiter", fallbackMethod = "fallbackUpdateMovie")
    public ResponseEntity<MovieResponse> updateMovie(String id, UpdateMovie updateMovie) {
        logger.atInfo().log("Received request to update movie: {}", updateMovie);
        try {
            Movie m = moviesService.updateMovie(id, updateMovie);
            logger.atInfo().log("Movie with id '{}' updated: {}", m.getId(), m);
            return new ResponseEntity<>(m, HttpStatus.OK);
        } catch (Exception ex) {
            logger.atError().withThrowable(ex).log("Error updating movie with id '{}': {}", id, updateMovie);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Error updating movie with id " + id);
            errorResponse.setError("Bad Request");
            errorResponse.setTimestamp(OffsetDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @RateLimiter(name = "moviesRateLimiter", fallbackMethod = "fallbackDeleteMovie")
    public ResponseEntity<ErrorResponse> deleteMovie(String id) {
        logger.atInfo().log("Received request to delete movie with id: {}", id);
        try {
            moviesService.deleteMovie(id);
            logger.atInfo().log("Movie with id '{}' deleted", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            logger.atError().withThrowable(ex).log("Error deleting movie with id: {}", id);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Error deleting movie with id " + id);
            errorResponse.setError("Bad Request");
            errorResponse.setTimestamp(OffsetDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // Fallback methods - each must match the exact signature of the original method + Throwable
    public ResponseEntity<MovieResponse> fallbackGetMovieById(String id, Throwable t) {
        return createRateLimitErrorResponse();
    }

    public ResponseEntity<MoviesResponse> fallbackGetMovies(Throwable t) {
        ErrorResponse errorResponse = createErrorResponse();
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    public ResponseEntity<MovieResponse> fallbackCreateMovie(CreateMovie createMovie, Throwable t) {
        return createRateLimitErrorResponse();
    }

    public ResponseEntity<MovieResponse> fallbackUpdateMovie(String id, UpdateMovie updateMovie, Throwable t) {
        return createRateLimitErrorResponse();
    }

    public ResponseEntity<ErrorResponse> fallbackDeleteMovie(String id, Throwable t) {
        ErrorResponse errorResponse = createErrorResponse();
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    private ResponseEntity<MovieResponse> createRateLimitErrorResponse() {
        ErrorResponse errorResponse = createErrorResponse();
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    private ErrorResponse createErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Rate limit exceeded. Please try again later.");
        errorResponse.setError("Too Many Requests");
        errorResponse.setTimestamp(OffsetDateTime.now());
        return errorResponse;
    }
}
