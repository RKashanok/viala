package com.viala.controller;

import com.viala.controller.dto.ApiResponse;
import com.viala.controller.dto.DrugCompatibilityRequest;
import com.viala.controller.dto.SymptomRequest;
import com.viala.service.OpenAiApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openai")
public class OpenAiController {

    private final OpenAiApiService openAiApiService;

    @Autowired
    public OpenAiController(OpenAiApiService openAiApiService) {
        this.openAiApiService = openAiApiService;
    }

    @PostMapping("/check-compatibility")
    public ResponseEntity<ApiResponse<String>> checkCompatibility(@RequestBody DrugCompatibilityRequest request) {
        String response = openAiApiService.getDrugCompatibility(String.join(", ", request.getDrugNames()));
        return ResponseEntity.ok(new ApiResponse<>(true, response, null));
    }

    @PostMapping("/select-drug")
    public ResponseEntity<ApiResponse<String>> selectDrug(@RequestBody SymptomRequest request) {
        String response = openAiApiService.getDrugSelectionForSymptoms(request.getSymptomDescription(), String.join(", ", request.getCurrentMedications()));
        return ResponseEntity.ok(new ApiResponse<>(true, response, null));
    }
}
