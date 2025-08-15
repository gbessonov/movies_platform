package io.github.gbessonov.movies_platform.movies.unit.services;

import io.github.gbessonov.movies_platform.movies.repositories.MoviesRepository;
import io.github.gbessonov.movies_platform.movies.services.impl.BasicMoviesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BasicMoviesServiceTests {

    @Test
    public void testNoMovies() {
        var repositoryMock = mock(MoviesRepository.class);
        when(repositoryMock.get()).thenReturn(Collections.emptyList());

        var subject = new BasicMoviesService(repositoryMock);
        var movies = subject.getMovies();

        Assertions.assertNotNull(movies);
        Assertions.assertTrue(movies.isEmpty());
    }
}
