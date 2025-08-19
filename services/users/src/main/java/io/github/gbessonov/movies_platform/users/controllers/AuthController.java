package io.github.gbessonov.movies_platform.users.controllers;

import io.github.gbessonov.movies_platform.authz.api.AuthApi;
import io.github.gbessonov.movies_platform.authz.model.*;
import io.github.gbessonov.movies_platform.users.services.AuthService;
import io.github.gbessonov.movies_platform.users.services.UsersService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
public class AuthController implements AuthApi {
    private final Logger logger = LogManager.getLogger(AuthController.class);

    private final AuthService authService;
    private final UsersService usersService;

    @Autowired
    public AuthController(AuthService authService, UsersService usersService) {
        this.authService = authService;
        this.usersService = usersService;
    }

    @Override
    @RateLimiter(name="authRateLimiter", fallbackMethod = "fallbackUserLogin")
    public ResponseEntity<LoginResponse> userLogin(LoginRequest loginRequest) {
        try {
            logger.info("login user with username: {}", loginRequest.getUsername());
            AuthService.AuthResponse result = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            LoginSuccessResponse resp = new LoginSuccessResponse()
                    .token(result.getToken())
                    .username(result.getUsername())
                    .expiresAt(result.getExpiresAt());
            logger.info("user {} logged in successfully", loginRequest.getUsername());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logger.atError().withThrowable(e)
                    .log("Authentication failed for user: {}", loginRequest.getUsername());
            var resp = new ErrorResponse()
                    .message("Authentication failed")
                    .error("Something went wrong");
            return new ResponseEntity<>(
                    resp,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Override
    @RateLimiter(name="authRateLimiter", fallbackMethod = "fallbackUserRegister")
    public ResponseEntity<RegisterResponse> userRegister(RegisterRequest registerRequest) {
        try {
            logger.info("Registering user with username: {}", registerRequest.getUsername());
            usersService.registerUser(registerRequest);
            logger.info("User {} registered successfully", registerRequest.getUsername());
            return ResponseEntity.ok(
                    new RegisterSuccessResponse()
                            .message("User registered successfully")
            );
        }catch (Exception e) {
            logger.atError().withThrowable(e)
                    .log("Registration failed for user: {}", registerRequest.getUsername());
            var resp = new ErrorResponse()
                    .message("Registration failed")
                    .error(e.getMessage());
            return new ResponseEntity<>(
                    resp,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // Fallback methods
    public ResponseEntity<LoginResponse> fallbackUserLogin(LoginRequest loginRequest, Throwable t) {
        ErrorResponse errorResponse = createErrorResponse();
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    public ResponseEntity<RegisterResponse> fallbackUserRegister(RegisterRequest registerRequest, Throwable t) {
        ErrorResponse errorResponse = createErrorResponse();
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    private ErrorResponse createErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Rate limit exceeded. Please try again later.");
        errorResponse.setError("Too Many Requests");
        errorResponse.setTimestamp(OffsetDateTime.now());
        return errorResponse;
    }
}
