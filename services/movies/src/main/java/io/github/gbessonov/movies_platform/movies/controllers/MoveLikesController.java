package io.github.gbessonov.movies_platform.movies.controllers;

import io.github.gbessonov.movies_platform.movies.api.MovieLikesApi;
import io.github.gbessonov.movies_platform.movies.model.ErrorResponse;
import io.github.gbessonov.movies_platform.movies.services.MovieLikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController()
public class MoveLikesController implements MovieLikesApi {
    private final MovieLikesService movieLikesService;

    @Autowired
    public MoveLikesController(MovieLikesService movieLikesService) {
        this.movieLikesService = movieLikesService;
    }

    @Override
    public ResponseEntity<ErrorResponse> likeMovie(String id){
        try {
            movieLikesService.likeMovie("38ff5540-1a9e-4021-8be7-8fce368edda2", id);
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
            movieLikesService.unlikeMovie("38ff5540-1a9e-4021-8be7-8fce368edda2", id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorResponse()
                    .message(e.getMessage())
                    .error("Bad Request")
                    .timestamp(java.time.OffsetDateTime.now()));
        }
    }
}
