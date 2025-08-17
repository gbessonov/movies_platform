package io.github.gbessonov.movies_platform.users.services.impl;

import io.github.gbessonov.movies_platform.users.model.AuthUser;
import io.github.gbessonov.movies_platform.users.repositories.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BasicUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    public BasicUserDetailsService(UsersRepository repository) {
        this.usersRepository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.getUserByName(username)
                .map(u -> new AuthUser(username, u.password))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
