package io.github.gbessonov.movies_platform.users.services;

import io.github.gbessonov.movies_platform.authz.model.RegisterRequest;
import io.github.gbessonov.movies_platform.users.model.CreateUserRequest;
import io.github.gbessonov.movies_platform.users.model.UpdateUserRequest;
import io.github.gbessonov.movies_platform.users.model.User;

import java.util.List;

public interface UsersService {
    List<User> getUsers();
    User addUser(CreateUserRequest user) throws Exception;
    void registerUser(RegisterRequest user) throws Exception;
    User updateUser(String id, UpdateUserRequest user) throws Exception;
    void deleteUser(String id);
}
