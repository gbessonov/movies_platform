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
    public void likeMovie(String userName, UUID movieId) {
        entityManager.createNativeQuery(
                "INSERT INTO movies_likes (user_name, movie_id) VALUES (:userName, :movieId) ON CONFLICT (user_name, movie_id) DO NOTHING")
                .setParameter("userName", userName)
                .setParameter("movieId", movieId)
                .executeUpdate();
    }

    @Override
    public void unlikeMovie(String userId, UUID movieId) {
        entityManager.createNativeQuery(
                "DELETE FROM movies_likes WHERE user_name = :userName AND movie_id = :movieId")
                .setParameter("userName", userId)
                .setParameter("movieId", movieId)
                .executeUpdate();
    }
}
