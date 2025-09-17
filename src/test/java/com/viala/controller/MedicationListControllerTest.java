package com.viala.controller;

import com.viala.controller.dto.ApiResponse;
import com.viala.controller.dto.JwtAuthenticationResponse;
import com.viala.controller.dto.LoginRequest;
import com.viala.model.MedicationList;
import com.viala.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class MedicationListControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.liquibase.enabled", () -> true);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword("password");
        user.setAge(30);
        user.setGender(com.viala.model.Gender.MALE);

        restTemplate.postForEntity("/api/users/register", user, ApiResponse.class);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser@test.com");
        loginRequest.setPassword("password");

        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity("/api/auth/login", loginRequest, JwtAuthenticationResponse.class);
        jwtToken = response.getBody().getAccessToken();
    }

    @Test
    void shouldCreateMedicationList() {
        MedicationList medicationList = new MedicationList();
        medicationList.setName("Test List");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<MedicationList> request = new HttpEntity<>(medicationList, headers);

        ResponseEntity<ApiResponse> response = restTemplate.postForEntity("/api/lists", request, ApiResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().isSuccess()).isTrue();
    }
}
