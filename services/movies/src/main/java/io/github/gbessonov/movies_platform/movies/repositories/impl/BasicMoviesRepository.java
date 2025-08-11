package io.github.gbessonov.movies_platform.movies.repositories.impl;

import io.github.gbessonov.movies_platform.movies.entities.DbMovie;
import io.github.gbessonov.movies_platform.movies.repositories.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class BasicMoviesRepository implements MoviesRepository {
    private final JpaMoviesRepository db;

    @Autowired
    public BasicMoviesRepository(JpaMoviesRepository jpaMoviesRepository) {
        this.db = jpaMoviesRepository;
    }

    @Override
    public List<DbMovie> getMovies() {
        return db.findAll();
    }

    @Override
    public Optional<DbMovie> getMovie(UUID id) {
        return db.findById(id);
    }
}
