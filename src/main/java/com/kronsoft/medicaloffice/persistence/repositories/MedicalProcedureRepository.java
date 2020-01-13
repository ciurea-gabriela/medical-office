package com.kronsoft.medicaloffice.persistence.repositories;

import com.kronsoft.medicaloffice.persistence.entity.MedicalProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalProcedureRepository extends JpaRepository<MedicalProcedure, Long> {
  boolean existsByName(String name);

  Optional<MedicalProcedure> findByName(String name);
}
