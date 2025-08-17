package io.github.gbessonov.movies_platform.users.controllers;

import io.github.gbessonov.movies_platform.authz.api.AuthApi;
import io.github.gbessonov.movies_platform.authz.model.LoginRequest;
import io.github.gbessonov.movies_platform.authz.model.LoginResponse;
import io.github.gbessonov.movies_platform.users.repositories.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class AuthController implements AuthApi {
    private final UsersRepository repository;


    public AuthController(UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<LoginResponse> userLogin(LoginRequest loginRequest) throws Exception {
        return AuthApi.super.userLogin(loginRequest);
    }
}
