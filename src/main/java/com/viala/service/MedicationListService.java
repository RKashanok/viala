package com.viala.service;

import com.viala.model.MedicationList;
import com.viala.repository.MedicationListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationListService {

    private final MedicationListRepository medicationListRepository;

    @Autowired
    public MedicationListService(MedicationListRepository medicationListRepository) {
        this.medicationListRepository = medicationListRepository;
    }

    public MedicationList saveMedicationList(MedicationList medicationList) {
        SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return medicationListRepository.save(medicationList);
    }

    public List<MedicationList> getMedicationLists() {
        // This needs to be updated to only return lists for the current user
        return medicationListRepository.findAll();
    }

    public void deleteMedicationList(Long id) {
        medicationListRepository.deleteById(id);
    }
}
