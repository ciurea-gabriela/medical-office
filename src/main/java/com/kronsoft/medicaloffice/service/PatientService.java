package com.kronsoft.medicaloffice.service;

import com.kronsoft.medicaloffice.dto.PatientContentDTO;
import com.kronsoft.medicaloffice.dto.PatientDTO;
import com.kronsoft.medicaloffice.exception.ForbiddenEventException;
import com.kronsoft.medicaloffice.exception.ResourceAlreadyExistsException;
import com.kronsoft.medicaloffice.exception.ResourceNotFoundException;
import com.kronsoft.medicaloffice.persistence.entity.Patient;
import com.kronsoft.medicaloffice.persistence.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
  private PatientRepository patientRepository;

  @Autowired
  public PatientService(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }

  public PatientDTO createPatient(PatientContentDTO patientContent)
      throws ResourceAlreadyExistsException {
    if (patientRepository.existsByCnp(patientContent.getCnp())) {
      throw new ResourceAlreadyExistsException("Cnp is invalid or already in use.");
    }

    Patient patient =
        patientRepository.save(
            new Patient(
                patientContent.getFirstName(),
                patientContent.getLastName(),
                patientContent.getBirthDate(),
                patientContent.getCnp(),
                patientContent.getSex(),
                patientContent.getCity(),
                patientContent.getPhoneNumber()));

    return new PatientDTO(
        patient.getId(),
        patient.getFirstName(),
        patient.getLastName(),
        patient.getBirthDate(),
        patient.getCnp(),
        patient.getSex(),
        patient.getCity(),
        patient.getPhoneNumber());
  }

  public PatientDTO getPatient(Long id) throws ResourceNotFoundException {
    Patient patient = findById(id);

    return new PatientDTO(
        patient.getId(),
        patient.getFirstName(),
        patient.getLastName(),
        patient.getBirthDate(),
        patient.getCnp(),
        patient.getSex(),
        patient.getCity(),
        patient.getPhoneNumber());
  }

  public List<PatientDTO> getAllPatients() {
    List<Patient> patients = patientRepository.findAll();

    List<PatientDTO> patientDTOS = new ArrayList<>();

    for (Patient patient : patients) {
      patientDTOS.add(
          new PatientDTO(
              patient.getId(),
              patient.getFirstName(),
              patient.getLastName(),
              patient.getBirthDate(),
              patient.getCnp(),
              patient.getSex(),
              patient.getCity(),
              patient.getPhoneNumber()));
    }

    return patientDTOS;
  }

  public void updatePatient(PatientContentDTO patientContent, Long id)
      throws ForbiddenEventException, ResourceNotFoundException {
    Patient patient = findById(id);
    if (!patient.getCnp().equals(patientContent.getCnp())
        && patientRepository.existsByCnp(patientContent.getCnp())) {
      throw new ForbiddenEventException("Cnp already in use");
    }

    patient.setFirstName(patientContent.getFirstName());
    patient.setLastName(patientContent.getLastName());
    patient.setBirthDate(patientContent.getBirthDate());
    patient.setCnp(patientContent.getCnp());
    patient.setSex(patientContent.getSex());
    patient.setCity(patientContent.getCity());
    patient.setPhoneNumber(patientContent.getPhoneNumber());

    patientRepository.save(patient);
  }

  public void deletePatient(Long id) {
    if (!patientRepository.existsById(id)) {
      return;
    }
    patientRepository.deleteById(id);
  }

  public Patient findById(Long id) {
    return patientRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Patient not found for id " + id));
  }

  public Patient save(Patient patient) {
    return patientRepository.save(patient);
  }
}
