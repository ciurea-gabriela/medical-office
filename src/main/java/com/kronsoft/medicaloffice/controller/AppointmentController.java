package com.kronsoft.medicaloffice.controller;

import com.kronsoft.medicaloffice.dto.AppointmentContentDTO;
import com.kronsoft.medicaloffice.dto.AppointmentDTO;
import com.kronsoft.medicaloffice.dto.AppointmentUpdateDTO;
import com.kronsoft.medicaloffice.dto.AppointmentWithPatientInfoDTO;
import com.kronsoft.medicaloffice.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AppointmentController {
  private AppointmentService appointmentService;

  @Autowired
  public AppointmentController(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  @PostMapping("/patients/{id}/appointments")
  @ResponseStatus(HttpStatus.CREATED)
  public AppointmentDTO createAppointment(
      @Valid @RequestBody AppointmentContentDTO appointmentContent,
      @PathVariable("id") Long patientId) {
    return appointmentService.createAppointment(appointmentContent, patientId);
  }

  @GetMapping("/patients/{id}/appointments/{appointmentId}")
  public AppointmentDTO getAppointment(
      @PathVariable("id") Long patientId, @PathVariable("appointmentId") Long appointmentId) {
    return appointmentService.getAppointment(patientId, appointmentId);
  }

  @GetMapping("/patients/{id}/appointments")
  public List<AppointmentDTO> getAllAppointments(@PathVariable("id") Long patientId) {
    return appointmentService.getAllAppointments(patientId);
  }

  @GetMapping("/appointments")
  public List<AppointmentWithPatientInfoDTO> getAllAppointmentsWithPatientInfo() {
    return appointmentService.getAllAppointmentsWithPatientInfo();
  }

  @PutMapping("/patients/{id}/appointments/{appointmentId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateAppointment(
      @Valid @RequestBody AppointmentContentDTO appointmentContent,
      @PathVariable("id") Long patientId,
      @PathVariable("appointmentId") Long appointmentId) {
    appointmentService.updateAppointmentFromPatient(appointmentContent, patientId, appointmentId);
  }

  @PutMapping("/appointments/{appointmentId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateAppointment(
      @PathVariable("appointmentId") Long appointmentId,
      @Valid @RequestBody AppointmentUpdateDTO appointmentContent) {
    appointmentService.updateAppointment(appointmentId, appointmentContent);
  }

  @DeleteMapping("/patients/{id}/appointments/{appointmentId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAppointment(
      @PathVariable("id") Long patientId, @PathVariable("appointmentId") Long appointmentId) {
    appointmentService.deleteAppointment(patientId, appointmentId);
  }
}
