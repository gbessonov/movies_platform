package io.github.gbessonov.movies_platform.movies.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll() //TODO: decide if we want to secure actuator endpoints
                        .requestMatchers("/api/movies").permitAll()
                        .requestMatchers("/api/movies/**").permitAll()
                        //TODO: ENABLE AUTHORIZATION
//                        .requestMatchers(HttpMethod.GET, "/api/movies/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/movies/**").hasAnyAuthority("SCOPE_movies:write", "ROLE_ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/api/movies/**").hasAnyAuthority("SCOPE_movies:write", "ROLE_ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/movies/**").hasAnyAuthority("SCOPE_movies:write", "ROLE_ADMIN")
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
