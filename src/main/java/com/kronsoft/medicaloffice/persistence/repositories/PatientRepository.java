package com.kronsoft.medicaloffice.persistence.repositories;

import com.kronsoft.medicaloffice.persistence.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
  boolean existsByCnp(String cnp);

  Optional<Patient> findByCnp(String cnp);
}
