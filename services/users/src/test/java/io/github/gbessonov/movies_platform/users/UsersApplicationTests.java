package io.github.gbessonov.movies_platform.users;

import io.github.gbessonov.movies_platform.users.controllers.AuthController;
import io.github.gbessonov.movies_platform.users.controllers.UsersController;
import io.github.gbessonov.movies_platform.users.repositories.UsersRepository;
import io.github.gbessonov.movies_platform.users.services.AuthService;
import io.github.gbessonov.movies_platform.users.services.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest()
@ActiveProfiles("test")
class UsersApplicationTests {

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
        assertThat(applicationContext.getBean(UsersService.class)).isNotNull();
        assertThat(applicationContext.getBean(UsersController.class)).isNotNull();
        assertThat(applicationContext.getBean(UsersRepository.class)).isNotNull();
        assertThat(applicationContext.getBean(AuthService.class)).isNotNull();
        assertThat(applicationContext.getBean(AuthController.class)).isNotNull();
        assertThat(applicationContext.getBean(UserDetailsService.class)).isNotNull();
    }

}
