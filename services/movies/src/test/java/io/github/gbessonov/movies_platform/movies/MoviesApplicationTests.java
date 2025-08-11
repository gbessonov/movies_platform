package io.github.gbessonov.movies_platform.movies;

import io.github.gbessonov.movies_platform.movies.controllers.MoviesController;
import io.github.gbessonov.movies_platform.movies.repositories.MoviesRepository;
import io.github.gbessonov.movies_platform.movies.services.MoviesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest()
@ActiveProfiles("test")
class MoviesApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;


    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    void essentialBeansAreCreated() {
        assertThat(applicationContext.getBean(MoviesService.class)).isNotNull();
        assertThat(applicationContext.getBean(MoviesController.class)).isNotNull();
        assertThat(applicationContext.getBean(MoviesRepository.class)).isNotNull();
    }

}
