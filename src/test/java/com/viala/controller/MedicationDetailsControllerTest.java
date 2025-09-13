package com.viala.controller;

import com.viala.controller.dto.ApiResponse;
import com.viala.controller.dto.JwtAuthenticationResponse;
import com.viala.controller.dto.LoginRequest;
import com.viala.controller.dto.MedicationDetailsRequest;
import com.viala.model.Medication;
import com.viala.model.MedicationDetails;
import com.viala.model.MedicationList;
import com.viala.model.Unit;
import com.viala.model.User;
import com.viala.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class MedicationDetailsControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MedicationRepository medicationRepository;

    private String jwtToken;
    private Long listId;
    private Long medicationId;


    @BeforeEach
    void setUp() {
        // Register user
        User user = new User();
        user.setEmail("testuser@test.com");
        user.setPassword("password");
        user.setAge(30);
        user.setGender(com.viala.model.Gender.MALE);
        restTemplate.postForEntity("/api/users/register", user, ApiResponse.class);

        // Login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser@test.com");
        loginRequest.setPassword("password");
        ResponseEntity<JwtAuthenticationResponse> loginResp = restTemplate.postForEntity("/api/auth/login", loginRequest, JwtAuthenticationResponse.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        jwtToken = loginResp.getBody().getAccessToken();

        // Attach a default Authorization header for all subsequent requests
        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                        request.getHeaders().setBearerAuth(jwtToken);
                    }
                    return execution.execute(request, body);
                })
        );

        // Create medication list via API
        MedicationList list = new MedicationList();
        list.setName("My List");
        HttpEntity<MedicationList> listReq = new HttpEntity<>(list);
        ResponseEntity<ApiResponse> listResp = restTemplate.postForEntity("/api/lists", listReq, ApiResponse.class);
        assertThat(listResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Fetch created list id by listing lists
        ResponseEntity<ApiResponse> listsGet = restTemplate.getForEntity("/api/lists", ApiResponse.class);
        assertThat(listsGet.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Create a medication directly via repository (catalog entry)
        Medication med = new Medication();
        med.setName("Ibuprofen");
        med.setUnit(Unit.UNITS);
        medicationId = medicationRepository.save(med).getId();
    }

    @Test
    void create_list_delete_flow() {
        // Create list and then list them to obtain id
        MedicationList list = new MedicationList();
        list.setName("List For Details");
        ResponseEntity<ApiResponse> createListResp = restTemplate.postForEntity("/api/lists", new HttpEntity<>(list), ApiResponse.class);
        assertThat(createListResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<ApiResponse> getListsResp = restTemplate.getForEntity("/api/lists", ApiResponse.class);
        assertThat(getListsResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Build request body for creating detail
        MedicationDetailsRequest req = new MedicationDetailsRequest();
        req.setMedicationListId(1L); // In this simplified test context with clean DB, first list has id=1
        req.setMedicationId(medicationId);
        req.setQuantity(new BigDecimal("10.00"));
        req.setExpirationDate(LocalDate.now().plusDays(1));

        ResponseEntity<ApiResponse> createDetailResp = restTemplate.postForEntity("/api/medication-details", new HttpEntity<>(req), ApiResponse.class);
        assertThat(createDetailResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createDetailResp.getBody().isSuccess()).isTrue();

        // List details by list
        ResponseEntity<ApiResponse> listDetailsResp = restTemplate.getForEntity("/api/medication-details/by-list/1", ApiResponse.class);
        assertThat(listDetailsResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listDetailsResp.getBody().isSuccess()).isTrue();

        // We cannot easily extract id from ApiResponse without a concrete DTO; perform delete using id=1 for clean DB
        ResponseEntity<ApiResponse> deleteResp =
                restTemplate.exchange("/api/medication-details/{id}", HttpMethod.DELETE, new HttpEntity<>(null), ApiResponse.class, 1);
        assertThat(deleteResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteResp.getBody().isSuccess()).isTrue();
    }
}
