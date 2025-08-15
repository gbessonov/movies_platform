package io.github.gbessonov.movies_platform.users.repositories;

import io.github.gbessonov.movies_platform.users.entities.DbUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository {
    List<DbUser> getMovies();
    Optional<DbUser> getMovie(UUID id);
}
