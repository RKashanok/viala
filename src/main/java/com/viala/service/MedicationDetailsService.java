package com.viala.service;

import com.viala.controller.dto.MedicationDetailsRequest;
import com.viala.model.Medication;
import com.viala.model.MedicationDetails;
import com.viala.model.MedicationList;
import com.viala.model.User;
import com.viala.repository.MedicationDetailsRepository;
import com.viala.repository.MedicationListRepository;
import com.viala.repository.MedicationRepository;
import com.viala.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicationDetailsService {

    private final MedicationDetailsRepository medicationDetailsRepository;
    private final MedicationListRepository medicationListRepository;
    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;

    public MedicationDetailsService(MedicationDetailsRepository medicationDetailsRepository,
                                    MedicationListRepository medicationListRepository,
                                    MedicationRepository medicationRepository,
                                    UserRepository userRepository) {
        this.medicationDetailsRepository = medicationDetailsRepository;
        this.medicationListRepository = medicationListRepository;
        this.medicationRepository = medicationRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth != null ? auth.getName() : null;
        return email != null ? userRepository.findByEmail(email).orElse(null) : null;
    }

    @Transactional
    public MedicationDetails create(MedicationDetailsRequest req) {
        User current = getCurrentUser();
        if (current == null) throw new IllegalStateException("Authenticated user not found");

        MedicationList list = medicationListRepository.findById(req.getMedicationListId())
                .orElseThrow(() -> new IllegalArgumentException("Medication list not found"));
        if (!list.getUser().getId().equals(current.getId())) {
            throw new SecurityException("You do not own this medication list");
        }

        Medication medication = medicationRepository.findById(req.getMedicationId())
                .orElseThrow(() -> new IllegalArgumentException("Medication not found"));

        MedicationDetails details = new MedicationDetails();
        details.setMedicationList(list);
        details.setMedication(medication);
        details.setUser(current);
        details.setQuantity(req.getQuantity());
        details.setExpirationDate(req.getExpirationDate());
        return medicationDetailsRepository.save(details);
    }

    @Transactional(readOnly = true)
    public List<MedicationDetails> listByMedicationList(Long listId) {
        User current = getCurrentUser();
        if (current == null) throw new IllegalStateException("Authenticated user not found");
        MedicationList list = medicationListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("Medication list not found"));
        if (!list.getUser().getId().equals(current.getId())) {
            throw new SecurityException("You do not own this medication list");
        }
        return medicationDetailsRepository.findByMedicationList_Id(listId);
    }

    @Transactional
    public void delete(Long detailsId) {
        User current = getCurrentUser();
        if (current == null) throw new IllegalStateException("Authenticated user not found");
        MedicationDetails details = medicationDetailsRepository.findById(detailsId)
                .orElseThrow(() -> new IllegalArgumentException("Medication details not found"));
        if (!details.getMedicationList().getUser().getId().equals(current.getId())) {
            throw new SecurityException("You do not own this medication list");
        }
        medicationDetailsRepository.deleteById(detailsId);
    }
}
