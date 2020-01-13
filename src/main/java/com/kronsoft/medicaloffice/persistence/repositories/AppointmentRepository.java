package com.kronsoft.medicaloffice.persistence.repositories;

import com.kronsoft.medicaloffice.persistence.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
  Optional<Appointment> findByIdAndPatientId(Long appointmentId, Long patientId);

  Optional<List<Appointment>> findByPatientId(Long patientId);
}
