package io.github.gbessonov.movies_platform.movies.repositories.impl;

import io.github.gbessonov.movies_platform.movies.entities.DbMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaMoviesRepository extends JpaRepository<DbMovie, UUID> {
}
