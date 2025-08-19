package io.github.gbessonov.movies_platform.movies.repositories;

import java.util.UUID;

public interface MovieLikesRepository {
    void likeMovie(String userName, UUID movieId);
    void unlikeMovie(String userName, UUID movieId);
}
