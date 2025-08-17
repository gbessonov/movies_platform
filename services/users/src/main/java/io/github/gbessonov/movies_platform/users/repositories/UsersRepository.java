package io.github.gbessonov.movies_platform.users.repositories;

import io.github.gbessonov.movies_platform.users.entities.DbUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository {
    List<DbUser> getUsers();
    Optional<DbUser> getUserById(UUID id);
    Optional<DbUser> getUserByName(String name);
    DbUser save(DbUser user);
    void deleteUser(UUID id);
    boolean existsByName(String name);
}
