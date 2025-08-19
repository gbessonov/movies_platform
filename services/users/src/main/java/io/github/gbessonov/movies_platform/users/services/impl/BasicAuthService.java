package io.github.gbessonov.movies_platform.users.services.impl;

import io.github.gbessonov.movies_platform.users.security.JwtTokenService;
import io.github.gbessonov.movies_platform.users.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class BasicAuthService implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public BasicAuthService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    public AuthResponse authenticate(String username, String password) {
        var token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);

        String jwtToken = jwtTokenService.generateToken(authentication);
        Long expiresAt = jwtTokenService.extractExpirationTime(jwtToken);

        return new AuthResponse(
                jwtToken,
                authentication.getName(),
                expiresAt
        );
    }

    public static class AuthResponse implements AuthService.AuthResponse {
        private final String token;
        private final String username;
        private final Long expiresAt;

        public AuthResponse(String token, String username, Long expiresAt) {
            this.token = token;
            this.username = username;
            this.expiresAt = expiresAt;
        }

        public String getToken() {
            return token;
        }

        public String getUsername() {
            return username;
        }

        public Long getExpiresAt() {
            return expiresAt;
        }
    }
}
