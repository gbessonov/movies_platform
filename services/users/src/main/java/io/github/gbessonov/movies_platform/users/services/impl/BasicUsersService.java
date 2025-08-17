package io.github.gbessonov.movies_platform.users.services.impl;

import io.github.gbessonov.movies_platform.users.entities.DbUser;
import io.github.gbessonov.movies_platform.users.model.CreateUserRequest;
import io.github.gbessonov.movies_platform.users.model.UpdateUserRequest;
import io.github.gbessonov.movies_platform.users.model.User;
import io.github.gbessonov.movies_platform.users.repositories.UsersRepository;
import io.github.gbessonov.movies_platform.users.services.UsersService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class BasicUsersService implements UsersService {

    private final UsersRepository repository;

    @Autowired
    public BasicUsersService(UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getUsers() {
        return repository.getUsers().stream().map(BasicUsersService::mapToUser).toList();
    }

    @Override
    public User addUser(CreateUserRequest user) throws Exception {
        // Check if user with the same name already exists
        if (repository.existsByName(user.getName())) {
            throw new IllegalArgumentException("User with name '" + user.getName() + "' already exists");
        }
        DbUser dbUser = new DbUser();
        dbUser.name = user.getName();
        setHashAndSalt(dbUser, user.getPassword());
        return mapToUser(repository.save(dbUser));
    }

    @Override
    public User updateUser(String id, UpdateUserRequest user) throws Exception{
        UUID safeId = UUID.fromString(id);
        DbUser existingDbUser = repository.getUserById(safeId)
                .orElseThrow(() -> new IllegalArgumentException("User with id '" + id + "' not found"));

        // Update DbUser entity
        existingDbUser.name = user.getName();
        setHashAndSalt(existingDbUser, user.getPassword());
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

    @Override
    public Optional<User> getUserByName(String name) {
        return repository.getUserByName(name).map(BasicUsersService::mapToUser);
    }

    //TODO: Consider safer and better password handling
    // 1. Using a more secure password hashing algorithm (e.g., bcrypt, Argon2)
    // 2. Implementing proper error handling and logging
    private static void setHashAndSalt(DbUser user, String password) throws Exception {
        // Generate salt for password hashing
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // Hash the password with the salt
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        // Set the hash and salt in the user entity
        user.passwordHash = Base64.getEncoder().encodeToString(hash);
        user.salt = Base64.getEncoder().encodeToString(salt);
    }

    private static User mapToUser(DbUser dbUser) {
        return new User()
                .id(dbUser.id)
                .name(dbUser.name);
    }

}
