package com.kronsoft.medicaloffice.service;

import com.kronsoft.medicaloffice.dto.MedicalProcedureContentDTO;
import com.kronsoft.medicaloffice.dto.MedicalProcedureDTO;
import com.kronsoft.medicaloffice.exception.ForbiddenEventException;
import com.kronsoft.medicaloffice.exception.ResourceAlreadyExistsException;
import com.kronsoft.medicaloffice.exception.ResourceNotFoundException;
import com.kronsoft.medicaloffice.persistence.entity.MedicalProcedure;
import com.kronsoft.medicaloffice.persistence.repositories.MedicalProcedureRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicalProcedureService {
  private MedicalProcedureRepository medicalProcedureRepository;

  public MedicalProcedureService(MedicalProcedureRepository medicalProcedureRepository) {
    this.medicalProcedureRepository = medicalProcedureRepository;
  }

  public MedicalProcedureDTO createMedicalProcedure(
      MedicalProcedureContentDTO medicalProcedureContent) throws ResourceAlreadyExistsException {

    if (medicalProcedureRepository.existsByName(medicalProcedureContent.getName())) {
      throw new ResourceAlreadyExistsException("Medical procedure already exists");
    }

    MedicalProcedure medicalProcedure =
        medicalProcedureRepository.save(
            new MedicalProcedure(
                medicalProcedureContent.getName(),
                medicalProcedureContent.getPrice(),
                medicalProcedureContent.getDescription()));

    return new MedicalProcedureDTO(
        medicalProcedure.getId(),
        medicalProcedure.getName(),
        medicalProcedure.getPrice(),
        medicalProcedure.getDescription());
  }

  public MedicalProcedureDTO getMedicalProcedure(Long id) throws ResourceNotFoundException {
    MedicalProcedure medicalProcedure =
        medicalProcedureRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medical procedure not found"));

    return new MedicalProcedureDTO(
        medicalProcedure.getId(),
        medicalProcedure.getName(),
        medicalProcedure.getPrice(),
        medicalProcedure.getDescription());
  }

  public List<MedicalProcedureDTO> getAllMedicalProcedures() {
    List<MedicalProcedure> medicalProcedures = medicalProcedureRepository.findAll();

    List<MedicalProcedureDTO> medicalProcedureDTOS = new ArrayList<>();

    for (MedicalProcedure mp : medicalProcedures) {
      medicalProcedureDTOS.add(
          new MedicalProcedureDTO(mp.getId(), mp.getName(), mp.getPrice(), mp.getDescription()));
    }

    return medicalProcedureDTOS;
  }

  public void updateMedicalProcedure(MedicalProcedureContentDTO medicalProcedureContent, Long id)
      throws ForbiddenEventException, ResourceNotFoundException {
    MedicalProcedure medicalProcedure =
        medicalProcedureRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medical Procedure not found"));
    if (!medicalProcedure.getName().equals(medicalProcedureContent.getName())
        && medicalProcedureRepository.existsByName(medicalProcedureContent.getName())) {
      throw new ForbiddenEventException("This medical procedure is already in use");
    }

    medicalProcedure.setName(medicalProcedureContent.getName());
    medicalProcedure.setPrice(medicalProcedureContent.getPrice());
    medicalProcedure.setDescription(medicalProcedureContent.getDescription());

    medicalProcedureRepository.save(medicalProcedure);
  }

  public void deleteMedicalProcedure(Long id) {
    if (!medicalProcedureRepository.existsById(id)) {
      return;
    }
    medicalProcedureRepository.deleteById(id);
  }
}
