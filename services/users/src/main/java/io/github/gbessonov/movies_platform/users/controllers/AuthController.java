package io.github.gbessonov.movies_platform.users.controllers;

import io.github.gbessonov.movies_platform.authz.api.AuthApi;
import io.github.gbessonov.movies_platform.authz.model.*;
import io.github.gbessonov.movies_platform.users.services.AuthService;
import io.github.gbessonov.movies_platform.users.services.UsersService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController()
@RateLimiter(name="authRateLimiter", fallbackMethod = "fallbackMoviesRateLimiter")
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
            AuthService.AuthResponse result = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            LoginSuccessResponse resp = new LoginSuccessResponse()
                    .token(result.getToken())
                    .username(result.getUsername())
                    .expiresAt(result.getExpiresAt());
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

    public ResponseEntity<ErrorResponse> fallbackMoviesRateLimiter(String something, Throwable t) {
        // Used by RateLimiter to handle rate limit exceeded errors
        // This method will be called when the rate limit is exceeded
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Rate limit exceeded. Please try again later.");
        errorResponse.setError("Too Many Requests");
        errorResponse.setTimestamp(OffsetDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }
}
