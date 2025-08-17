package io.github.gbessonov.movies_platform.users.controllers;

import io.github.gbessonov.movies_platform.users.api.UsersApi;
import io.github.gbessonov.movies_platform.users.model.*;
import io.github.gbessonov.movies_platform.users.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
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

    @Override
    public ResponseEntity<UserResponse> addUser(CreateUserRequest createUserRequest) {
        try{
            User user = usersService.addUser(createUserRequest);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse()
                    .message(e.getMessage())
                    .error("Bad Request")
                    .timestamp(OffsetDateTime.now()));
        }
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(String id, UpdateUserRequest updateUserRequest) {
        try{
            User user = usersService.updateUser(id, updateUserRequest);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse()
                    .message(e.getMessage())
                    .error("Bad Request")
                    .timestamp(OffsetDateTime.now()));
        }
    }

    @Override
    public ResponseEntity<ErrorResponse> deleteUser(String id) {
        try {
            usersService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse()
                    .message(e.getMessage())
                    .error("Bad Request")
                    .timestamp(OffsetDateTime.now()));
        }
    }
}
