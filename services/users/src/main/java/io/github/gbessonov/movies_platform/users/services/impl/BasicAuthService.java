package io.github.gbessonov.movies_platform.users.services.impl;

import io.github.gbessonov.movies_platform.users.security.JwtTokenService;
import io.github.gbessonov.movies_platform.users.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class BasicAuthService implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public BasicAuthService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    //TODO: add username, expires etc
    public String authenticate(String username, String password) {
        var token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);

        String jwtToken = jwtTokenService.generateToken(authentication);
        Long expiresAt = jwtTokenService.extractExpirationTime(jwtToken);

        //return new AuthResponse(jwtToken, authentication.getName(), expiresAt);
        return jwtToken;
    }
}
