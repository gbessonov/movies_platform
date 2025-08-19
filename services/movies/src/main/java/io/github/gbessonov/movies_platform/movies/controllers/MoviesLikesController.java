package io.github.gbessonov.movies_platform.movies.controllers;

import io.github.gbessonov.movies_platform.movies.api.MovieLikesApi;
import io.github.gbessonov.movies_platform.movies.model.ErrorResponse;
import io.github.gbessonov.movies_platform.movies.services.MovieLikesService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController()
public class MoviesLikesController implements MovieLikesApi {
    private static final Logger logger = LogManager.getLogger(MoviesLikesController.class);

    private final MovieLikesService movieLikesService;

    @Autowired
    public MoviesLikesController(MovieLikesService movieLikesService) {
        this.movieLikesService = movieLikesService;
    }

    @Override
    @RateLimiter(name="moviesLikesRateLimiter", fallbackMethod = "fallbackLikeMovie")
    public ResponseEntity<ErrorResponse> likeMovie(String id){
        try {
            logger.info("Movies like movie with id: {}", id);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            movieLikesService.likeMovie(userName, id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            logger.atError().withThrowable(e)
                    .log("Error liking movie with id: {}", id);
            return ResponseEntity.badRequest().body(new ErrorResponse()
                    .message(e.getMessage())
                    .error("Bad Request")
                    .timestamp(java.time.OffsetDateTime.now()));
        }
    }

    @Override
    @RateLimiter(name="moviesLikesRateLimiter", fallbackMethod = "fallbackUnlikeMovie")
    public ResponseEntity<ErrorResponse> unlikeMovie(String id){
        try {
            logger.info("Movies unlike movie with id: {}", id);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            movieLikesService.unlikeMovie(userName, id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            logger.atError().withThrowable(e)
                    .log("Error unliking movie with id: {}", id);
            return ResponseEntity.badRequest().body(new ErrorResponse()
                    .message(e.getMessage())
                    .error("Bad Request")
                    .timestamp(java.time.OffsetDateTime.now()));
        }
    }

    public ResponseEntity<ErrorResponse> fallbackLikeMovie(String id, Throwable t) {
        return createRateLimitErrorResponse();
    }

    public ResponseEntity<ErrorResponse> fallbackUnlikeMovie(String id, Throwable t) {
        return createRateLimitErrorResponse();
    }

    private ResponseEntity<ErrorResponse> createRateLimitErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse()
                .message("Rate limit exceeded. Please try again later.")
                .error("Too Many Requests")
                .timestamp(OffsetDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }
}
