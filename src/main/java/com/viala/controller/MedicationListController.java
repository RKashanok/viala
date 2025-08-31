package com.viala.controller;

import com.viala.controller.dto.ApiResponse;
import com.viala.model.Medication;
import com.viala.model.MedicationList;
import com.viala.service.MedicationListService;
import com.viala.service.MedicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class MedicationListController {

    private final MedicationListService medicationListService;
    private final MedicationService medicationService;

    @Autowired
    public MedicationListController(MedicationListService medicationListService, MedicationService medicationService) {
        this.medicationListService = medicationListService;
        this.medicationService = medicationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MedicationList>> createMedicationList(@Valid @RequestBody MedicationList medicationList) {
        MedicationList newList = medicationListService.saveMedicationList(medicationList);
        return ResponseEntity.ok(new ApiResponse<>(true, newList, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicationList>>> getMedicationLists() {
        List<MedicationList> lists = medicationListService.getMedicationLists();
        return ResponseEntity.ok(new ApiResponse<>(true, lists, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMedicationList(@PathVariable Long id) {
        medicationListService.deleteMedicationList(id);
        return ResponseEntity.ok(new ApiResponse<>(true, null, null));
    }

    @PostMapping("/{listId}/medications")
    public ResponseEntity<ApiResponse<Medication>> addMedicationToList(@PathVariable Long listId, @Valid @RequestBody Medication medication) {
        Medication newMedication = medicationListService.addMedicationToList(listId, medication);
        return ResponseEntity.ok(new ApiResponse<>(true, newMedication, null));
    }

    @GetMapping("/{listId}/medications")
    public ResponseEntity<ApiResponse<List<Medication>>> getMedicationsFromList(@PathVariable Long listId) {
        List<Medication> medications = medicationListService.getMedicationsFromList(listId);
        return ResponseEntity.ok(new ApiResponse<>(true, medications, null));
    }
}
