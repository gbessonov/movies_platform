package io.github.gbessonov.movies_platform.users.services.impl;

import io.github.gbessonov.movies_platform.users.model.User;
import io.github.gbessonov.movies_platform.users.repositories.UsersRepository;
import io.github.gbessonov.movies_platform.users.services.UsersService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return repository.getMovies().stream()
                .map(e -> new User()
                        .id(e.id)
                        .name(e.name)
                ).toList();
    }
}
