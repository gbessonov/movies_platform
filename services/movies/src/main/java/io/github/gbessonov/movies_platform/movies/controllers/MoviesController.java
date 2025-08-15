package io.github.gbessonov.movies_platform.movies.controllers;

import io.github.gbessonov.movies_platform.movies.api.MoviesApi;
import io.github.gbessonov.movies_platform.movies.model.*;
import io.github.gbessonov.movies_platform.movies.services.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RestController()
public class MoviesController implements MoviesApi {

    private final MoviesService moviesService;

    @Autowired
    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @Override
    public ResponseEntity<MovieResponse> getMovieById(String id) {
        Optional<Movie> movie = moviesService.getMovieById(id);

        if (movie.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Movie with id '" + id + "' not found");
            errorResponse.setError("Not Found");
            errorResponse.setTimestamp(OffsetDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(movie.get(), org.springframework.http.HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Movie>> getMovies() {
        //TODO: decide what to do with trailing slashes
        List<Movie> movies = moviesService.getMovies();

        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MovieResponse> createMovie(CreateMovie createMovie) {
        try {
            Movie m = moviesService.createMovie(createMovie);
            return new ResponseEntity<>(m, HttpStatus.CREATED);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setError("Bad Request");
            errorResponse.setTimestamp(OffsetDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<MovieResponse> updateMovie(String id, UpdateMovie updateMovie) {
        if (id == null || id.isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Movie m = moviesService.updateMovie(id, updateMovie);
            return new ResponseEntity<>(m, HttpStatus.OK);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setError("Bad Request");
            errorResponse.setTimestamp(OffsetDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ErrorResponse> deleteMovie(String id) {
        if (id == null || id.isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            moviesService.deleteMovie(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setError("Bad Request");
            errorResponse.setTimestamp(OffsetDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
