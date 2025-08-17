package io.github.gbessonov.movies_platform.users.services;

public interface AuthService {
    String authenticate(String username, String password);
}
