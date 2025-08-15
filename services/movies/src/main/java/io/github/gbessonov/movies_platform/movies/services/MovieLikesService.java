package io.github.gbessonov.movies_platform.movies.services;

public interface MovieLikesService {
    void likeMovie(String userId, String movieId);
    void unlikeMovie(String userId, String movieId);
}
