package com.kronsoft.medicaloffice.controller;

import com.kronsoft.medicaloffice.dto.PatientContentDTO;
import com.kronsoft.medicaloffice.dto.PatientDTO;
import com.kronsoft.medicaloffice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PatientController {

  private PatientService patientService;

  @Autowired
  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }

  @PostMapping("/patients")
  @ResponseStatus(HttpStatus.CREATED)
  public PatientDTO createPatient(@Valid @RequestBody PatientContentDTO patientContent) {
    return patientService.createPatient(patientContent);
  }

  @GetMapping("/patients/{id}")
  public PatientDTO getPatient(@PathVariable("id") Long id) {
    return patientService.getPatient(id);
  }

  @GetMapping("/patients")
  public List<PatientDTO> getPatients() {
    return patientService.getAllPatients();
  }

  @PutMapping("/patients/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updatePatient(
      @Valid @RequestBody PatientContentDTO patientContent, @PathVariable("id") Long id) {
    patientService.updatePatient(patientContent, id);
  }

  @DeleteMapping("/patients/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePatient(@PathVariable("id") Long id) {
    patientService.deletePatient(id);
  }
}
