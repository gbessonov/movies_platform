package io.github.gbessonov.movies_platform.movies.repositories.impl;

import io.github.gbessonov.movies_platform.movies.entities.DbMovie;
import io.github.gbessonov.movies_platform.movies.repositories.MoviesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BasicMoviesRepository implements MoviesRepository {
    private final JpaMoviesRepository db;

    @Autowired
    public BasicMoviesRepository(JpaMoviesRepository jpaMoviesRepository) {
        this.db = jpaMoviesRepository;
    }

    @Override
    public List<DbMovie> get() {
        return db.findAll();
    }

    @Override
    public Optional<DbMovie> get(UUID id) {
        return db.findById(id);
    }

    @Override
    public DbMovie save(DbMovie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null");
        }
        return db.save(movie);
    }

    @Override
    public void deleteById(UUID id) {
        db.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return db.existsById(id);
    }
}
