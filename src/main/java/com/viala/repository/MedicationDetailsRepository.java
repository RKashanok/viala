package com.viala.repository;

import com.viala.model.MedicationDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicationDetailsRepository extends JpaRepository<MedicationDetails, Long> {
    List<MedicationDetails> findByMedicationList_Id(Long medicationListId);
    List<MedicationDetails> findByUser_Id(Long userId);
    List<MedicationDetails> findByMedication_Id(Long medicationId);
    Optional<MedicationDetails> findByMedicationList_IdAndMedication_Id(Long medicationListId, Long medicationId);
}
