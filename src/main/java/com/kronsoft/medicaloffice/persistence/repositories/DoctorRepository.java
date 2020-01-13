package com.kronsoft.medicaloffice.persistence.repositories;

import com.kronsoft.medicaloffice.persistence.entity.Doctor;
import com.kronsoft.medicaloffice.persistence.entity.MedicalProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
  boolean existsByCnp(String cnp);

  Optional<Doctor> findByCnp(String cnp);

  List<Doctor> findAllByMedicalProceduresContains(MedicalProcedure medicalProcedure);
}
