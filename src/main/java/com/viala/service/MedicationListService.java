package com.viala.service;

import com.viala.model.Medication;
import com.viala.model.MedicationList;
import com.viala.repository.MedicationListRepository;
import com.viala.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationListService {

    private final MedicationListRepository medicationListRepository;
    private final MedicationRepository medicationRepository;

    @Autowired
    public MedicationListService(MedicationListRepository medicationListRepository, MedicationRepository medicationRepository) {
        this.medicationListRepository = medicationListRepository;
        this.medicationRepository = medicationRepository;
    }

    public MedicationList saveMedicationList(MedicationList medicationList) {
        return medicationListRepository.save(medicationList);
    }

    public List<MedicationList> getMedicationLists() {
        // This needs to be updated to only return lists for the current user
        return medicationListRepository.findAll();
    }

    public void deleteMedicationList(Long id) {
        medicationListRepository.deleteById(id);
    }

    public Medication addMedicationToList(Long listId, Medication medication) {
        MedicationList medicationList = medicationListRepository.findById(listId).orElse(null);
        if (medicationList != null) {
            medication.setList(medicationList);
            return medicationRepository.save(medication);
        }
        return null;
    }

    public List<Medication> getMedicationsFromList(Long listId) {
        return medicationRepository.findByListId(listId);
    }
}
