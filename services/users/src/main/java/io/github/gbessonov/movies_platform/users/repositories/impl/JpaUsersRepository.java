package io.github.gbessonov.movies_platform.users.repositories.impl;

import io.github.gbessonov.movies_platform.users.entities.DbUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaUsersRepository extends JpaRepository<DbUser, UUID> {
}
