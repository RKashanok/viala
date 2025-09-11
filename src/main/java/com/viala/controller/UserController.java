package com.viala.controller;

import com.viala.controller.dto.ApiResponse;
import com.viala.controller.dto.RegisterResponse;
import com.viala.controller.dto.SharingRequest;
import com.viala.model.MedicationList;
import com.viala.model.Sharing;
import com.viala.model.User;
import com.viala.repository.MedicationListRepository;
import com.viala.repository.UserRepository;
import com.viala.service.SharingService;
import com.viala.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SharingService sharingService;
    private final UserRepository userRepository;
    private final MedicationListRepository medicationListRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, SharingService sharingService, UserRepository userRepository, MedicationListRepository medicationListRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.sharingService = sharingService;
        this.userRepository = userRepository;
        this.medicationListRepository = medicationListRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerUser(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(new ApiResponse<>(true, new RegisterResponse(savedUser.getId()), null));
    }

    @PostMapping("/share")
    public ResponseEntity<ApiResponse<Sharing>> shareMedicationList(@RequestBody SharingRequest sharingRequest) {
        String ownerUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(ownerUsername).orElse(null);
        User sharedWith = userRepository.findByEmail(sharingRequest.getEmail()).orElse(null);
        MedicationList medicationList = medicationListRepository.findById(sharingRequest.getMedicationListId()).orElse(null);

        if (owner != null && sharedWith != null && medicationList != null) {
            Sharing sharing = new Sharing();
            sharing.setMedicationList(medicationList);
            sharing.setSharedWith(sharedWith);
            sharing.setPermission(sharingRequest.getPermission());
            Sharing newSharing = sharingService.saveSharing(sharing);
            return ResponseEntity.ok(new ApiResponse<>(true, newSharing, null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, null, "User or Medication List not found."));
        }
    }
}
