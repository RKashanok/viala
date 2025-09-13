package com.viala.controller;

import com.viala.controller.dto.ApiResponse;
import com.viala.controller.dto.MedicationDetailsRequest;
import com.viala.model.MedicationDetails;
import com.viala.service.MedicationDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/medication-details")
public class MedicationDetailsController {

    private final MedicationDetailsService medicationDetailsService;

    public MedicationDetailsController(MedicationDetailsService medicationDetailsService) {
        this.medicationDetailsService = medicationDetailsService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MedicationDetails>> create(@Valid @RequestBody MedicationDetailsRequest request) {
        MedicationDetails created = medicationDetailsService.create(request);
        return ResponseEntity.ok(new ApiResponse<>(true, created, null));
    }

    @GetMapping("/by-list/{listId}")
    public ResponseEntity<ApiResponse<List<MedicationDetails>>> listByList(@PathVariable Long listId) {
        List<MedicationDetails> items = medicationDetailsService.listByMedicationList(listId);
        return ResponseEntity.ok(new ApiResponse<>(true, items, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        medicationDetailsService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, null, null));
    }
}
