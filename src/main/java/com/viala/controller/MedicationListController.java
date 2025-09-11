package com.viala.controller;

import com.viala.controller.dto.ApiResponse;
import com.viala.model.MedicationDetail;
import com.viala.model.MedicationList;
import com.viala.model.User;
import com.viala.repository.UserRepository;
import com.viala.service.MedicationDetailService;
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
    private final MedicationDetailService medicationDetailService;
    private final UserRepository userRepository;

    @Autowired
    public MedicationListController(MedicationListService medicationListService, MedicationDetailService medicationDetailService, UserRepository userRepository) {
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

    @PostMapping("/{listId}/medications")
    public ResponseEntity<ApiResponse<MedicationDetail>> addMedicationToList(@PathVariable Long listId, @Valid @RequestBody MedicationDetail medicationDetail) {
        MedicationDetail newMedicationDetail = medicationDetailService.saveMedicationDetail(medicationDetail);
        return ResponseEntity.ok(new ApiResponse<>(true, newMedicationDetail, null));
    }

    @GetMapping("/{listId}/medications")
    public ResponseEntity<ApiResponse<List<MedicationDetail>>> getMedicationsFromList(@PathVariable Long listId) {
        List<MedicationDetail> medicationDetails = medicationDetailService.getMedicationDetailsFromList(listId);
        return ResponseEntity.ok(new ApiResponse<>(true, medicationDetails, null));
    }
}
