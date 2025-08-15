package io.github.gbessonov.movies_platform.users.controllers;

import io.github.gbessonov.movies_platform.users.api.UsersApi;
import io.github.gbessonov.movies_platform.users.model.User;
import io.github.gbessonov.movies_platform.users.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class UsersController implements UsersApi {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = usersService.getUsers();
        return ResponseEntity.ok(users);
    }
}
