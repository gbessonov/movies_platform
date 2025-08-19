package io.github.gbessonov.movies_platform.users.controllers;

import io.github.gbessonov.movies_platform.users.api.UsersApi;
import io.github.gbessonov.movies_platform.users.model.*;
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
public class UsersController implements UsersApi {
    private static final Logger logger = LogManager.getLogger(UsersController.class);

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    @RateLimiter(name = "usersRateLimiter", fallbackMethod = "fallbackGetUsers")
    public ResponseEntity<UsersResponse> getUsers() {
        logger.info("Fetching all users");
        UsersListResponse resp = UsersListResponse.fromList(usersService.getUsers());
        logger.info("Successfully fetched {} users", resp.getSize());
        return ResponseEntity.ok(resp);
    }

    @Override
    @RateLimiter(name = "usersRateLimiter", fallbackMethod = "fallbackAddUser")
    public ResponseEntity<UserResponse> addUser(CreateUserRequest createUserRequest) {
        try {
            logger.info("Creating user: {}", createUserRequest);
            User user = usersService.addUser(createUserRequest);
            logger.info("Successfully created user: {}", user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.atError().withThrowable(e)
                    .log("Failed to create user: {}", createUserRequest.getName());
            return ResponseEntity.badRequest().body(new ErrorResponse()
                    .message("Failed to create user: " + createUserRequest.getName())
                    .error("Bad Request")
                    .timestamp(OffsetDateTime.now()));
        }
    }

    @Override
    @RateLimiter(name = "usersRateLimiter", fallbackMethod = "fallbackUpdateUser")
    public ResponseEntity<UserResponse> updateUser(String id, UpdateUserRequest updateUserRequest) {
        try {
            logger.info("Updating user with id: {}", id);
            User user = usersService.updateUser(id, updateUserRequest);
            logger.info("Successfully updated user with id: {}", id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.atError().withThrowable(e)
                    .log("Failed to update user with id: {}", id);
            return ResponseEntity.badRequest().body(new ErrorResponse()
                    .message("Failed to update user with id: " + id)
                    .error("Bad Request")
                    .timestamp(OffsetDateTime.now()));
        }
    }

    @Override
    @RateLimiter(name = "usersRateLimiter", fallbackMethod = "fallbackDeleteUser")
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

    // Fallback methods
    public ResponseEntity<UsersResponse> fallbackGetUsers(Throwable t) {
        ErrorResponse errorResponse = createErrorResponse();
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    public ResponseEntity<UserResponse> fallbackAddUser(CreateUserRequest createUserRequest, Throwable t) {
        ErrorResponse errorResponse = createErrorResponse();
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    public ResponseEntity<UserResponse> fallbackUpdateUser(String id, UpdateUserRequest updateUserRequest, Throwable t) {
        ErrorResponse errorResponse = createErrorResponse();
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    public ResponseEntity<ErrorResponse> fallbackDeleteUser(String id, Throwable t) {
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
