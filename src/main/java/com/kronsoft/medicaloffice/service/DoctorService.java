package com.kronsoft.medicaloffice.service;

import com.kronsoft.medicaloffice.dto.*;
import com.kronsoft.medicaloffice.exception.ForbiddenEventException;
import com.kronsoft.medicaloffice.exception.InvalidInputException;
import com.kronsoft.medicaloffice.exception.ResourceAlreadyExistsException;
import com.kronsoft.medicaloffice.exception.ResourceNotFoundException;
import com.kronsoft.medicaloffice.persistence.entity.Doctor;
import com.kronsoft.medicaloffice.persistence.entity.MedicalProcedure;
import com.kronsoft.medicaloffice.persistence.repositories.DoctorRepository;
import com.kronsoft.medicaloffice.persistence.repositories.MedicalProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.kronsoft.medicaloffice.persistence.entity.enums.UpdateOperation.ADD;
import static com.kronsoft.medicaloffice.persistence.entity.enums.UpdateOperation.DELETE;

@Service
public class DoctorService {
  private DoctorRepository doctorRepository;
  private MedicalProcedureRepository medicalProcedureRepository;

  @Autowired
  public DoctorService(
      DoctorRepository doctorRepository, MedicalProcedureRepository medicalProcedureRepository) {
    this.doctorRepository = doctorRepository;
    this.medicalProcedureRepository = medicalProcedureRepository;
  }

  public DoctorDTO createDoctor(DoctorCreateContentDTO doctorContent)
      throws ResourceAlreadyExistsException, ResourceNotFoundException {
    if (doctorRepository.existsByCnp(doctorContent.getCnp())) {
      throw new ResourceAlreadyExistsException("Cnp is invalid or already in use.");
    }

    MedicalProcedure medicalProcedure =
        medicalProcedureRepository
            .findById(doctorContent.getMedicalProcedureId())
            .orElseThrow(() -> new ResourceNotFoundException("Medical Procedure not found."));

    Doctor doctor =
        new Doctor(
            doctorContent.getName(),
            doctorContent.getCnp(),
            doctorContent.getSex(),
            doctorContent.getSpecialization(),
            doctorContent.getPhoneNumber());

    doctor.getMedicalProcedures().add(medicalProcedure);
    doctorRepository.save(doctor);

    return new DoctorDTO(
        doctor.getId(),
        doctor.getName(),
        doctor.getCnp(),
        doctor.getSex(),
        doctor.getSpecialization(),
        doctor.getPhoneNumber());
  }

  public DoctorDTO getDoctor(Long id) throws ResourceNotFoundException {
    Doctor doctor = findById(id);

    return new DoctorDTO(
        doctor.getId(),
        doctor.getName(),
        doctor.getCnp(),
        doctor.getSex(),
        doctor.getSpecialization(),
        doctor.getPhoneNumber());
  }

  public List<DoctorProcedureNamesDTO> getAllDoctorsWithMedicalProcedures() {
    List<Doctor> doctors = doctorRepository.findAll();
    return getDoctorProcedureNamesDTOS(doctors);
  }

  private List<DoctorProcedureNamesDTO> getDoctorProcedureNamesDTOS(List<Doctor> doctors) {
    List<DoctorProcedureNamesDTO> doctorsDTOS = new ArrayList<>();

    for (Doctor doctor : doctors) {
      List<String> medicalProcedures = new ArrayList<>();
      for (MedicalProcedure medicalProcedure : doctor.getMedicalProcedures()) {
        medicalProcedures.add(medicalProcedure.getName());
      }
      doctorsDTOS.add(
          new DoctorProcedureNamesDTO(
              doctor.getId(),
              doctor.getName(),
              doctor.getCnp(),
              doctor.getSex(),
              doctor.getSpecialization(),
              doctor.getPhoneNumber(),
              medicalProcedures));
    }
    return doctorsDTOS;
  }

  public List<DoctorProcedureNamesDTO> getDoctorsByMedicalProcedureId(Long medicalProcedureId) {
    MedicalProcedure medicalProcedure =
        medicalProcedureRepository
            .findById(medicalProcedureId)
            .orElseThrow(() -> new ResourceNotFoundException("Medical procedure not found"));
    List<Doctor> doctors = doctorRepository.findAllByMedicalProceduresContains(medicalProcedure);

    return getDoctorProcedureNamesDTOS(doctors);
  }

  public void updateDoctor(DoctorUpdateContentDTO doctorContent, Long doctorId)
      throws ResourceNotFoundException, ForbiddenEventException {
    Doctor doctor = findById(doctorId);

    if (!doctor.getCnp().equals(doctorContent.getCnp())
        && doctorRepository.existsByCnp(doctorContent.getCnp())) {
      throw new ForbiddenEventException("Cnp is invalid or already in use.");
    }

    doctor.setName(doctorContent.getName());
    doctor.setCnp(doctorContent.getCnp());
    doctor.setSex(doctorContent.getSex());
    doctor.setSpecialization(doctorContent.getSpecialization());
    doctor.setPhoneNumber(doctorContent.getPhoneNumber());

    doctorRepository.save(doctor);
  }

  public void deleteDoctor(Long id) {
    if (doctorRepository.existsById(id)) {
      return;
    }
    doctorRepository.deleteById(id);
  }

  public void updateMedicalProceduresWithinDoctor(
      Long doctorId, MedicalProceduresWithinDoctorDTO updateDTO) {

    if (ADD.equals(updateDTO.getUpdateOperation())) {
      addMedicalProcedure(doctorId, updateDTO.getMedicalProcedureId());
    } else if (DELETE.equals(updateDTO.getUpdateOperation())) {
      deleteMedicalProcedure(doctorId, updateDTO.getMedicalProcedureId());
    }
  }

  private void addMedicalProcedure(Long doctorId, Long medicalProcedureId)
      throws ResourceNotFoundException {
    MedicalProcedure medicalProcedure =
        medicalProcedureRepository
            .findById(medicalProcedureId)
            .orElseThrow(() -> new ResourceNotFoundException("Medical procedure not found"));

    Doctor doctor = findById(doctorId);

    for (MedicalProcedure mp : doctor.getMedicalProcedures()) {
      if (mp.getId().equals(medicalProcedureId)) {
        throw new InvalidInputException("Invalid input. Medical Procedure already added.");
      }
    }

    doctor.getMedicalProcedures().add(medicalProcedure);
    doctorRepository.save(doctor);
  }

  private void deleteMedicalProcedure(Long doctorId, Long medicalProcedureId) {
    Doctor doctor = findById(doctorId);

    doctor.getMedicalProcedures().removeIf(mp -> mp.getId().equals(medicalProcedureId));
    this.doctorRepository.save(doctor);
  }

  public Doctor findById(Long id) {
    return doctorRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Doctor not found for id " + id));
  }
}
