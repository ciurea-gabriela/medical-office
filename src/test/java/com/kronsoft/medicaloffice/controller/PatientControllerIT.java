package com.kronsoft.medicaloffice.controller;

import com.kronsoft.medicaloffice.dto.ErrorDTO;
import com.kronsoft.medicaloffice.dto.PatientContentDTO;
import com.kronsoft.medicaloffice.dto.PatientDTO;
import com.kronsoft.medicaloffice.persistence.entity.Patient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;

public class PatientControllerIT extends ControllerBaseIT {

  private static final String NEW_PATIENT_CNP = "4545555635845";
  private static final Long INVALID_PATIENT_ID = 99L;
  private static final String UPDATED_FIRST_NAME = "Updated patient";
  private static final String UPDATED_PHONE_NUMBER = "55555";
  private static final String PATIENT_UPDATE_CNP = "0000000000123";
  private static final int NUMBER_OF_PATIENTS = 1;
  private static final String CREATE_PATIENT_URI = "/patients";

  @Before
  public void beforeTests() {
    super.setup();
  }

  @After
  public void afterTests() {
    super.clear();
  }

  @Test
  public void testCreatePatientSuccessful() {
    PatientContentDTO patientContent =
        new PatientContentDTO(
                FIRST_NAME_PATIENT, LAST_NAME_PATIENT, BIRTH_DATE, NEW_PATIENT_CNP, SEX, CITY, PHONE_NUMBER);

    ResponseEntity<String> response =
        restTemplate.exchange(
            createPatientUri(CREATE_PATIENT_URI),
            HttpMethod.POST,
            new HttpEntity<>(patientContent),
            String.class);

    Patient patient = patientRepository.findByCnp(NEW_PATIENT_CNP).orElse(null);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(patient);
    assertEquals(NEW_PATIENT_CNP, patient.getCnp());
  }

  @Test
  public void testCreatePatientWithCnpAlreadyInUse() {
    PatientContentDTO patientContent =
        new PatientContentDTO(
                FIRST_NAME_PATIENT, LAST_NAME_PATIENT, BIRTH_DATE, TEST_PATIENT_CNP, SEX, CITY, PHONE_NUMBER);

    ResponseEntity<ErrorDTO> response =
        restTemplate.exchange(
            createPatientUri(CREATE_PATIENT_URI),
            HttpMethod.POST,
            new HttpEntity<>(patientContent),
            ErrorDTO.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testGetPatientSuccessful() {
    ResponseEntity<PatientDTO> response =
        restTemplate.exchange(
            createPatientUri(CREATE_PATIENT_URI + "/" + getTestPatient().getId()),
            HttpMethod.GET,
            null,
            PatientDTO.class);

    assertNotNull(response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(TEST_PATIENT_CNP, response.getBody().getCnp());
  }

  @Test
  public void testGetPatientNotFound() {
    ResponseEntity<ErrorDTO> response =
        restTemplate.exchange(
            createPatientUri(CREATE_PATIENT_URI + "/" + INVALID_PATIENT_ID),
            HttpMethod.GET,
            null,
            ErrorDTO.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testGetPatientsSuccessful() {
    ResponseEntity<List<PatientDTO>> response =
        restTemplate.exchange(
            createPatientUri(CREATE_PATIENT_URI),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<PatientDTO>>() {});

    assertEquals(NUMBER_OF_PATIENTS, response.getBody().size());
  }

  @Test
  public void testUpdatePatientSuccessful() {
    PatientContentDTO patientContent =
        new PatientContentDTO(
            UPDATED_FIRST_NAME,
            getTestPatient().getLastName(),
            getTestPatient().getBirthDate(),
            getTestPatient().getCnp(),
            getTestPatient().getSex(),
            getTestPatient().getCity(),
            UPDATED_PHONE_NUMBER);

    ResponseEntity<PatientContentDTO> response =
        restTemplate.exchange(
            createPatientUri(CREATE_PATIENT_URI + "/" + getTestPatient().getId()),
            HttpMethod.PUT,
            new HttpEntity<>(patientContent),
            PatientContentDTO.class);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    Patient patient = patientRepository.findByCnp(TEST_PATIENT_CNP).orElse(new Patient());
    assertEquals(TEST_PATIENT_CNP, patient.getCnp());
    assertEquals(UPDATED_FIRST_NAME, patient.getFirstName());
    assertEquals(UPDATED_PHONE_NUMBER, patient.getPhoneNumber());
  }

  @Test
  public void testUpdatePatientNotFound() {
    PatientContentDTO patientContent =
        new PatientContentDTO(
                FIRST_NAME_PATIENT, LAST_NAME_PATIENT, BIRTH_DATE, TEST_PATIENT_CNP, SEX, CITY, PHONE_NUMBER);

    ResponseEntity<ErrorDTO> response =
        restTemplate.exchange(
            createPatientUri(CREATE_PATIENT_URI + "/" + INVALID_PATIENT_ID),
            HttpMethod.PUT,
            new HttpEntity<>(patientContent),
            ErrorDTO.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testUpdatePatientWithForbiddenCnp() {
    createTestPatient(PATIENT_UPDATE_CNP);

    PatientContentDTO patientContent =
        new PatientContentDTO(
            getTestPatient().getFirstName(),
            getTestPatient().getLastName(),
            getTestPatient().getBirthDate(),
            PATIENT_UPDATE_CNP,
            getTestPatient().getSex(),
            getTestPatient().getCity(),
            getTestPatient().getPhoneNumber());

    ResponseEntity<ErrorDTO> response =
        restTemplate.exchange(
            createPatientUri(CREATE_PATIENT_URI + "/" + getTestPatient().getId()),
            HttpMethod.PUT,
            new HttpEntity<>(patientContent),
            ErrorDTO.class);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  public void testDeletePatientSuccessful() {
    ResponseEntity response =
        restTemplate.exchange(
            createPatientUri(CREATE_PATIENT_URI + "/" + getTestPatient().getId()),
            HttpMethod.DELETE,
            null,
            Void.class);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertFalse(patientRepository.existsByCnp(TEST_PATIENT_CNP));
  }
}
