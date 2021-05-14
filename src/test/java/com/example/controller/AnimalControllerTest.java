package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;

@EnableAutoConfiguration
@WebAppConfiguration
public class AnimalControllerTest {

    private static final String FOO_RESOURCE_URL = "http://localhost:8082";

    @Autowired
    private static TestRestTemplate testRestTemplate;

    @BeforeAll
    public static void setUp() {
        testRestTemplate = new TestRestTemplate();
    }

    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(FOO_RESOURCE_URL + "/api/v1/animals",
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}