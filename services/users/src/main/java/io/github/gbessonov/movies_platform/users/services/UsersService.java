package io.github.gbessonov.movies_platform.users.services;

import io.github.gbessonov.movies_platform.users.model.CreateUserRequest;
import io.github.gbessonov.movies_platform.users.model.UpdateUserRequest;
import io.github.gbessonov.movies_platform.users.model.User;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    List<User> getUsers();
    User addUser(CreateUserRequest user) throws Exception;
    User updateUser(String id, UpdateUserRequest user) throws Exception;
    void deleteUser(String id);
    Optional<User> getUserByName(String name);
}
