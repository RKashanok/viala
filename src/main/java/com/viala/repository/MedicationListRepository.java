package com.viala.repository;

import com.viala.model.MedicationList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationListRepository extends JpaRepository<MedicationList, Long> {
}
