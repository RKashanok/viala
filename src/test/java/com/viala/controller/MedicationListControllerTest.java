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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class MedicationListControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.profiles.active", () -> "test");
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.liquibase.enabled", () -> true);
        registry.add("spring.liquibase.change-log", () -> "classpath:db/changelog/db.changelog-master.xml");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.sql.init.mode", () -> "never");
        registry.add("logging.level.liquibase", () -> "DEBUG");
        registry.add("logging.level.org.springframework.boot.autoconfigure.liquibase", () -> "DEBUG");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        // Verify schema exists before proceeding
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "select count(*) from information_schema.tables where table_schema='public' and table_name='users'",
                    Integer.class
            );
            assertThat(count).isNotNull();
            assertThat(count).isGreaterThan(0);
        } catch (Exception e) {
            throw new AssertionError("Liquibase did not create 'users' table before tests. Root cause: " + e.getMessage(), e);
        }

        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword("password");
        user.setAge(30);
        user.setGender(com.viala.model.Gender.MALE);

        ResponseEntity<ApiResponse> registerResponse = restTemplate.postForEntity("/api/users/register", user, ApiResponse.class);
        assertThat(registerResponse.getStatusCode().is2xxSuccessful())
                .as("Registration should return 2xx, got: %s", registerResponse.getStatusCode())
                .isTrue();
        assertThat(registerResponse.getBody()).as("Registration body should not be null").isNotNull();
        assertThat(registerResponse.getBody().isSuccess()).as("Registration should be successful").isTrue();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser@test.com");
        loginRequest.setPassword("password");

        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity("/api/auth/login", loginRequest, JwtAuthenticationResponse.class);
        assertThat(response.getStatusCode().is2xxSuccessful())
                .as("Login should return 2xx, got: %s", response.getStatusCode())
                .isTrue();
        assertThat(response.getBody()).as("Login response body should not be null").isNotNull();
        jwtToken = response.getBody().getAccessToken();
        assertThat(jwtToken).as("JWT token should not be null or empty").isNotBlank();
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
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
    }
}
