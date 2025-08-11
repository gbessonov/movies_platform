package io.github.gbessonov.movies_platform.movies.controllers;

import io.github.gbessonov.movies_platform.movies.api.DefaultApi;
import io.github.gbessonov.movies_platform.movies.model.ErrorResponse;
import io.github.gbessonov.movies_platform.movies.model.Movie;
import io.github.gbessonov.movies_platform.movies.services.MoviesService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController()
public class MoviesController implements DefaultApi {

    private final MoviesService moviesService;

    @Autowired
    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @Override
    public ResponseEntity<Movie> getMovieById(String id) {
        Optional<Movie> movie = moviesService.getMovieById(id);

        if (movie.isEmpty()) {
            //TODO: fix response body
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(movie.get(), org.springframework.http.HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Movie>> getMovies() {
        List<Movie> movies = moviesService.getMovies();

        return new ResponseEntity<>(movies, org.springframework.http.HttpStatus.OK);
    }
}
