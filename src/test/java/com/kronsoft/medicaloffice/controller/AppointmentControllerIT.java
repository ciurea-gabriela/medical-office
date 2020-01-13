package com.kronsoft.medicaloffice.controller;

import com.kronsoft.medicaloffice.dto.AppointmentContentDTO;
import com.kronsoft.medicaloffice.dto.AppointmentDTO;
import com.kronsoft.medicaloffice.dto.ErrorDTO;
import com.kronsoft.medicaloffice.persistence.entity.Appointment;
import com.kronsoft.medicaloffice.persistence.entity.enums.Status;
import com.kronsoft.medicaloffice.persistence.repositories.AppointmentRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AppointmentControllerIT extends ControllerBaseIT {

  @Autowired private AppointmentRepository appointmentRepository;
  private static final Status STATUS = Status.CREATED;
  private static final LocalDateTime START_TIME = LocalDateTime.of(1990, 5, 24, 8, 30);
  private static final LocalDateTime END_TIME = LocalDateTime.of(1990, 5, 24, 10, 30);
  private static final String DESCRIPTION = "test description";
  private static final Long INVALID_PATIENT_ID = 53L;
  private static final Long INVALID_APPOINTMENT_ID = 74L;

  @Before
  public void beforeTests() {
    super.setup();
  }

  @After
  public void afterTests() {
    super.clear();
  }

  @Test
  public void testCreateAppointmentSuccessful() {
    final int NUMBER_OF_APPOINTMENTS = 1;

    AppointmentContentDTO appointmentContent =
        new AppointmentContentDTO(
            STATUS,
            START_TIME,
            END_TIME,
            DESCRIPTION,
            getTestDoctor().getId(),
            getTestMedicalProcedure().getId());

    ResponseEntity<AppointmentDTO> response =
        restTemplate.exchange(
            createAppointmentUri(getTestPatient().getId()),
            HttpMethod.POST,
            new HttpEntity<>(appointmentContent),
            AppointmentDTO.class);

    List<Appointment> appointments =
        appointmentRepository.findByPatientId(getTestPatient().getId()).orElseGet(ArrayList::new);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(NUMBER_OF_APPOINTMENTS, appointments.size());
    assertEquals(STATUS, appointments.get(0).getStatus());
  }

  @Test
  public void testCreateAppointmentPatientNotFound() {
    AppointmentContentDTO appointmentContent =
        new AppointmentContentDTO(
            STATUS,
            START_TIME,
            END_TIME,
            DESCRIPTION,
            getTestDoctor().getId(),
            getTestMedicalProcedure().getId());

    ResponseEntity<ErrorDTO> response =
        restTemplate.exchange(
            createAppointmentUri(INVALID_PATIENT_ID),
            HttpMethod.POST,
            new HttpEntity<>(appointmentContent),
            ErrorDTO.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testGetAppointmentSuccessful() {
    Appointment appointment = createAppointment();

    ResponseEntity<AppointmentDTO> response =
        restTemplate.exchange(
            createAppointmentUri(getTestPatient().getId(), appointment.getId()),
            HttpMethod.GET,
            null,
            AppointmentDTO.class);

    AppointmentDTO appointmentResponse = response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(appointment.getId(), appointmentResponse.getId());
    assertEquals(appointment.getStatus(), appointmentResponse.getStatus());
  }

  @Test
  public void testGetAppointmentPatientNotFound() {
    Appointment appointment = createAppointment();

    ResponseEntity<ErrorDTO> response =
        restTemplate.exchange(
            createAppointmentUri(INVALID_PATIENT_ID, appointment.getId()),
            HttpMethod.GET,
            null,
            ErrorDTO.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testGetAppointmentNotFound() {
    ResponseEntity<ErrorDTO> response =
        restTemplate.exchange(
            createAppointmentUri(getTestPatient().getId(), INVALID_APPOINTMENT_ID),
            HttpMethod.GET,
            null,
            ErrorDTO.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  private Appointment createAppointment() {
    return appointmentRepository.save(
        new Appointment(
            STATUS,
            START_TIME,
            END_TIME,
            DESCRIPTION,
            getTestPatient(),
            getTestDoctor(),
            getTestMedicalProcedure()));
  }

  private void createAppointments(int numberOfAppointments) {
    for (int i = 0; i < numberOfAppointments; i++) {
      createAppointment();
    }
  }

  private String createAppointmentUri(Long userId) {
    return "/patients/" + userId + "/appointments";
  }

  private String createAppointmentUri(Long userId, Long appointmentId) {
    return createAppointmentUri(userId) + "/" + appointmentId;
  }
}
