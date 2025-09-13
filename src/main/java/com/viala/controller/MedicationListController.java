package com.viala.controller;

import com.viala.controller.dto.ApiResponse;
import com.viala.model.MedicationDetails;
import com.viala.model.MedicationList;
import com.viala.model.User;
import com.viala.repository.UserRepository;
import com.viala.service.MedicationDetailsService;
import com.viala.service.MedicationListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class MedicationListController {

    private final MedicationListService medicationListService;
    private final MedicationDetailsService medicationDetailService;
    private final UserRepository userRepository;

    @Autowired
    public MedicationListController(MedicationListService medicationListService, MedicationDetailsService medicationDetailService, UserRepository userRepository) {
        this.medicationListService = medicationListService;
        this.medicationDetailService = medicationDetailService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MedicationList>> createMedicationList(@Valid @RequestBody MedicationList medicationList) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username).orElse(null);
        medicationList.setUser(user);
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

    @GetMapping("/{listId}/medications")
    public ResponseEntity<ApiResponse<List<MedicationDetails>>> getMedicationsFromList(@PathVariable Long listId) {
        List<MedicationDetails> medicationDetails = medicationDetailService.listByMedicationList(listId);
        return ResponseEntity.ok(new ApiResponse<>(true, medicationDetails, null));
    }
}
