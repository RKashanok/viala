package com.viala.service;

import com.viala.model.Medication;
import com.viala.model.MedicationList;
import com.viala.repository.MedicationListRepository;
import com.viala.repository.MedicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicationListServiceTest {

    @Mock
    private MedicationListRepository medicationListRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private MedicationListService medicationListService;

    @Test
    void saveMedicationList() {
        MedicationList medicationList = new MedicationList();
        medicationListService.saveMedicationList(medicationList);
        verify(medicationListRepository).save(medicationList);
    }

    @Test
    void addMedicationToList() {
        MedicationList medicationList = new MedicationList();
        when(medicationListRepository.findById(1L)).thenReturn(Optional.of(medicationList));
        Medication medication = new Medication();
        medicationListService.addMedicationToList(1L, medication);
        verify(medicationRepository).save(medication);
    }
}
