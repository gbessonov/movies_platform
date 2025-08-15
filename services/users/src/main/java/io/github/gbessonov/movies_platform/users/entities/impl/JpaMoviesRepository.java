package io.github.gbessonov.movies_platform.users.entities.impl;

import io.github.gbessonov.movies_platform.users.entities.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaMoviesRepository extends JpaRepository<DbUser, UUID> {
}
