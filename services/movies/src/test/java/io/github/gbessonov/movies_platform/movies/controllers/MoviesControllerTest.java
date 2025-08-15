package io.github.gbessonov.movies_platform.movies.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MoviesControllerTest {
    private final TestRestTemplate restTemplate;

    @Autowired
    public MoviesControllerTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    public void testInvalidId() {
        var TEST_ID = "12345678-some-uuid-goes-here~4174000";
        ResponseEntity<String> response = restTemplate.getForEntity("/api/movies/" + TEST_ID, String.class);

        // Assert the HTTP status
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Assert the response body contains expected error information
        String responseBody = response.getBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertTrue(responseBody.contains("Movie with id '" + TEST_ID + "' not found"));
        Assertions.assertTrue(responseBody.contains("Not Found"));
        Assertions.assertTrue(responseBody.contains("timestamp"));
    }
}
