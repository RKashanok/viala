package com.viala.repository;

import com.viala.model.Sharing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharingRepository extends JpaRepository<Sharing, Long> {
}
