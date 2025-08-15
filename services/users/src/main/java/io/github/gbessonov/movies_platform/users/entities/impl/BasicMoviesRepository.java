package io.github.gbessonov.movies_platform.users.entities.impl;

import io.github.gbessonov.movies_platform.users.entities.DbUser;
import io.github.gbessonov.movies_platform.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BasicMoviesRepository implements UsersRepository {
    private final JpaMoviesRepository db;

    @Autowired
    public BasicMoviesRepository(JpaMoviesRepository jpaMoviesRepository) {
        this.db = jpaMoviesRepository;
    }

    @Override
    public List<DbUser> getMovies() {
        return db.findAll();
    }

    @Override
    public Optional<DbUser> getMovie(UUID id) {
        return db.findById(id);
    }
}
