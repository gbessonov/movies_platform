package io.github.gbessonov.movies_platform.users.services;

public interface AuthService {
    AuthResponse authenticate(String username, String password);

    interface AuthResponse {
        String getToken();
        String getUsername();
        Long getExpiresAt();
    }
}
