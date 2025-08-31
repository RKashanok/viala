package com.viala.service;

import com.viala.model.Medication;
import com.viala.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public Medication saveMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication updateQuantity(Long id, Double quantity) {
        Medication medication = medicationRepository.findById(id).orElse(null);
        if (medication != null) {
            medication.setQuantity(new BigDecimal(quantity));
            return medicationRepository.save(medication);
        }
        return null;
    }
}
