package io.github.gbessonov.movies_platform.users.controllers;

import io.github.gbessonov.movies_platform.authz.api.AuthApi;
import io.github.gbessonov.movies_platform.authz.model.*;
import io.github.gbessonov.movies_platform.users.model.CreateUserRequest;
import io.github.gbessonov.movies_platform.users.model.User;
import io.github.gbessonov.movies_platform.users.services.AuthService;
import io.github.gbessonov.movies_platform.users.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class AuthController implements AuthApi {
    private final AuthService authService;
    private final UsersService usersService;

    @Autowired
    public AuthController(AuthService authService, UsersService usersService) {
        this.authService = authService;
        this.usersService = usersService;
    }

    @Override
    public ResponseEntity<LoginResponse> userLogin(LoginRequest loginRequest) {
        try {
            String token = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            LoginSuccessResponse resp = new LoginSuccessResponse()
                    .token(token);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            var resp = new ErrorResponse()
                    .message("Authentication failed")
                    .error(e.getMessage());
            return new ResponseEntity<>(
                    resp,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Override
    public ResponseEntity<RegisterResponse> userRegister(RegisterRequest registerRequest) {
        try {
            usersService.registerUser(registerRequest);
            return ResponseEntity.ok(
                    new RegisterSuccessResponse()
                            .message("User registered successfully")
            );
        }catch (Exception e) {
            var resp = new ErrorResponse()
                    .message("Registration failed")
                    .error(e.getMessage());
            return new ResponseEntity<>(
                    resp,
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
