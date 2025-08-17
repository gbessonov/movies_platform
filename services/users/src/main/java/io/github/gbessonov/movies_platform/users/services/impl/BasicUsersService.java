package io.github.gbessonov.movies_platform.users.services.impl;

import io.github.gbessonov.movies_platform.authz.model.RegisterRequest;
import io.github.gbessonov.movies_platform.users.entities.DbUser;
import io.github.gbessonov.movies_platform.users.model.CreateUserRequest;
import io.github.gbessonov.movies_platform.users.model.UpdateUserRequest;
import io.github.gbessonov.movies_platform.users.model.User;
import io.github.gbessonov.movies_platform.users.repositories.UsersRepository;
import io.github.gbessonov.movies_platform.users.services.UsersService;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BasicUsersService implements UsersService {
    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BasicUsersService(UsersRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getUsers() {
        return repository.getUsers().stream().map(BasicUsersService::mapToUser).toList();
    }

    @Override
    public User addUser(CreateUserRequest user) throws Exception {
        return addNewUser(
                user.getName(),
                user.getPassword()
        );

    }

    @Override
    public void registerUser(RegisterRequest user) throws Exception {
        addNewUser(
                user.getUsername(),
                user.getPassword()
        );
    }

    @Override
    public User updateUser(String id, UpdateUserRequest user) throws Exception{
        UUID safeId = UUID.fromString(id);
        DbUser existingDbUser = repository.getUserById(safeId)
                .orElseThrow(() -> new IllegalArgumentException("User with id '" + id + "' not found"));

        // Update DbUser entity
        existingDbUser.name = user.getName();
        existingDbUser.password = user.getPassword();
        return mapToUser(repository.save(existingDbUser));
    }

    @Override
    public void deleteUser(String id) {
        UUID safeId = UUID.fromString(id);
        // Check if user exists before attempting to delete
        repository.getUserById(safeId)
                .orElseThrow(() -> new IllegalArgumentException("User with id '" + id + "' not found"));
        repository.deleteUser(safeId);
    }

    private User addNewUser(String name, String password) {
        // Check if user with the same name already exists
        if (repository.existsByName(name)) {
            throw new IllegalArgumentException("User with name '" + name + "' already exists");
        }
        DbUser dbUser = new DbUser();
        dbUser.name = name;
        dbUser.password = passwordEncoder.encode(password);
        return mapToUser(repository.save(dbUser));
    }

    private static User mapToUser(DbUser dbUser) {
        return new User()
                .id(dbUser.id)
                .name(dbUser.name);
    }
}
