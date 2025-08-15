package io.github.gbessonov.movies_platform.movies.repositories;

import java.util.UUID;

public interface MovieLikesRepository {
    void likeMovie(UUID userId, UUID movieId);
    void unlikeMovie(UUID userId, UUID movieId);
}
