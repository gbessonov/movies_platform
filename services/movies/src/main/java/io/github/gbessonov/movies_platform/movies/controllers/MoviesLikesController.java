package io.github.gbessonov.movies_platform.movies.controllers;

import io.github.gbessonov.movies_platform.movies.api.MovieLikesApi;
import io.github.gbessonov.movies_platform.movies.model.ErrorResponse;
import io.github.gbessonov.movies_platform.movies.services.MovieLikesService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController()
@RateLimiter(name="moviesLikesRateLimiter", fallbackMethod = "fallbackMoviesRateLimiter")
public class MoviesLikesController implements MovieLikesApi {
    private final MovieLikesService movieLikesService;

    @Autowired
    public MoviesLikesController(MovieLikesService movieLikesService) {
        this.movieLikesService = movieLikesService;
    }

    @Override
    public ResponseEntity<ErrorResponse> likeMovie(String id){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            movieLikesService.likeMovie(userName, id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse()
                    .message("Movie with id '" + id + "' not found")
                    .error("Not Found")
                    .timestamp(OffsetDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorResponse()
                    .message(e.getMessage())
                    .error("Bad Request")
                    .timestamp(java.time.OffsetDateTime.now()));
        }
    }

    @Override
    public ResponseEntity<ErrorResponse> unlikeMovie(String id){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();
            movieLikesService.unlikeMovie(userName, id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorResponse()
                    .message(e.getMessage())
                    .error("Bad Request")
                    .timestamp(java.time.OffsetDateTime.now()));
        }
    }

    public ResponseEntity<ErrorResponse> fallbackMoviesRateLimiter(String something, Throwable t) {
        // Used by RateLimiter to handle rate limit exceeded errors
        // This method will be called when the rate limit is exceeded
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Rate limit exceeded. Please try again later.");
        errorResponse.setError("Too Many Requests");
        errorResponse.setTimestamp(OffsetDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }
}
