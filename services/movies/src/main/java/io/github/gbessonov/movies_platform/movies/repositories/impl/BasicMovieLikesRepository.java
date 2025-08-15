package io.github.gbessonov.movies_platform.movies.repositories.impl;

import io.github.gbessonov.movies_platform.movies.repositories.MovieLikesRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class BasicMovieLikesRepository implements MovieLikesRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void likeMovie(UUID userId, UUID movieId) {
        entityManager.createNativeQuery(
                "INSERT INTO movies_likes (user_id, movie_id) VALUES (:userId, :movieId) ON CONFLICT (user_id, movie_id) DO NOTHING")
                .setParameter("userId", userId)
                .setParameter("movieId", movieId)
                .executeUpdate();
    }

    @Override
    public void unlikeMovie(UUID userId, UUID movieId) {
        entityManager.createNativeQuery(
                "DELETE FROM movies_likes WHERE user_id = :userId AND movie_id = :movieId")
                .setParameter("userId", userId)
                .setParameter("movieId", movieId)
                .executeUpdate();
    }
}
