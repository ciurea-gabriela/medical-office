package com.kronsoft.medicaloffice.controller;

import com.kronsoft.medicaloffice.dto.*;
import com.kronsoft.medicaloffice.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DoctorController {
  private DoctorService doctorService;

  @Autowired
  public DoctorController(DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  @PostMapping("/doctors")
  @ResponseStatus(HttpStatus.CREATED)
  public DoctorDTO createDoctor(@Valid @RequestBody DoctorCreateContentDTO doctorContent) {
    return doctorService.createDoctor(doctorContent);
  }

  @GetMapping("/doctors/{id}")
  public DoctorDTO getDoctor(@PathVariable("id") Long id) {
    return doctorService.getDoctor(id);
  }

  @GetMapping("/doctors")
  public List<DoctorProcedureNamesDTO> getAllDoctorsWithMedicalProcedures(
      @RequestParam(name = "medicalProcedureId", required = false) Long medicalProcedureId) {
    if (medicalProcedureId == null) {
      return doctorService.getAllDoctorsWithMedicalProcedures();
    }
    return doctorService.getDoctorsByMedicalProcedureId(medicalProcedureId);
  }

  @PutMapping("/doctors/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateDoctor(
      @Valid @RequestBody DoctorUpdateContentDTO doctorContent, @PathVariable("id") Long doctorId) {
    doctorService.updateDoctor(doctorContent, doctorId);
  }

  @DeleteMapping("/doctors")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDoctor(Long id) {
    doctorService.deleteDoctor(id);
  }

  @PatchMapping("/doctors/{id}/procedures")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateMedicalProceduresWithinDoctor(
      @PathVariable("id") Long doctorId, @Valid @RequestBody MedicalProceduresWithinDoctorDTO updateDTO) {
    doctorService.updateMedicalProceduresWithinDoctor(doctorId, updateDTO);
  }

}
