package com.viala.service;

import com.viala.model.MedicationList;
import com.viala.repository.MedicationListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicationListServiceTest {

    @Mock
    private MedicationListRepository medicationListRepository;

    @InjectMocks
    private MedicationListService medicationListService;

    @Test
    void saveMedicationList() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        MedicationList medicationList = new MedicationList();
        medicationListService.saveMedicationList(medicationList);
        verify(medicationListRepository).save(medicationList);
    }
}
