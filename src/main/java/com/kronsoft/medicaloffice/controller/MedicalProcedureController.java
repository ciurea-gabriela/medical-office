package com.kronsoft.medicaloffice.controller;

import com.kronsoft.medicaloffice.dto.MedicalProcedureContentDTO;
import com.kronsoft.medicaloffice.dto.MedicalProcedureDTO;
import com.kronsoft.medicaloffice.service.MedicalProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MedicalProcedureController {
  private MedicalProcedureService medicalProcedureService;

  @Autowired
  public MedicalProcedureController(MedicalProcedureService medicalProcedureService) {
    this.medicalProcedureService = medicalProcedureService;
  }

  @PostMapping("/medical-procedures")
  @ResponseStatus(HttpStatus.CREATED)
  public MedicalProcedureDTO createMedicalProcedure(
      @Valid @RequestBody MedicalProcedureContentDTO medicalProcedureContent) {
    return medicalProcedureService.createMedicalProcedure(medicalProcedureContent);
  }

  @GetMapping("/medical-procedures/{id}")
  public MedicalProcedureDTO getMedicalProcedure(@PathVariable("id") Long id) {
    return medicalProcedureService.getMedicalProcedure(id);
  }

  @GetMapping("/medical-procedures")
  public List<MedicalProcedureDTO> getAllMedicalProcedures() {
    return medicalProcedureService.getAllMedicalProcedures();
  }

  @PutMapping("/medical-procedures/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateMedicalProcedure(
      @Valid @RequestBody MedicalProcedureContentDTO medicalProcedureContent,
      @PathVariable("id") Long id) {
    medicalProcedureService.updateMedicalProcedure(medicalProcedureContent, id);
  }

  @DeleteMapping("/medical-procedures/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteMedicalProcedure(@PathVariable("id") Long id) {
    medicalProcedureService.deleteMedicalProcedure(id);
  }
}
