package com.kronsoft.medicaloffice.controller;

import com.kronsoft.medicaloffice.persistence.entity.Doctor;
import com.kronsoft.medicaloffice.persistence.entity.MedicalProcedure;
import com.kronsoft.medicaloffice.persistence.entity.Patient;
import com.kronsoft.medicaloffice.persistence.entity.enums.Sex;
import com.kronsoft.medicaloffice.persistence.repositories.DoctorRepository;
import com.kronsoft.medicaloffice.persistence.repositories.MedicalProcedureRepository;
import com.kronsoft.medicaloffice.persistence.repositories.PatientRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class ControllerBaseIT {

  @Autowired protected TestRestTemplate restTemplate;
  @Value("${server.port}") protected int port;

  @Autowired protected PatientRepository patientRepository;
  @Autowired protected DoctorRepository doctorRepository;
  @Autowired protected MedicalProcedureRepository medicalProcedureRepository;

  private Patient patient;
  private Doctor doctor;
  private MedicalProcedure medicalProcedure;
  protected static final String FIRST_NAME_PATIENT = "Patient T";
  protected static final String LAST_NAME_PATIENT = "Test";
  protected static final LocalDate BIRTH_DATE = LocalDate.of(1995, Month.MAY, 24);
  protected static final String TEST_PATIENT_CNP = "2405951005877";
  protected static final Sex SEX = Sex.FEMALE;
  protected static final String CITY = "BZ";
  protected static final String PHONE_NUMBER = "0000";
  protected static final String NAME_DOCTOR = "Doctor Test";
  protected static final String TEST_DOCTOR_CNP = "2950524874596";
  protected static final String SPECIALIZATION = "doctor";
  protected static final String NAME_MEDICAL_PROCEDURE = "Medical Procedure Test";
  protected static final String DESCRIPTION_MEDICAL_PROCEDURE = "Description";
  protected static final Double PRICE = 7.55D;

  protected void setup() {
    this.patient =
        createPatient(
            FIRST_NAME_PATIENT,
            LAST_NAME_PATIENT,
            BIRTH_DATE,
            TEST_PATIENT_CNP,
            SEX,
            CITY,
            PHONE_NUMBER);
    this.medicalProcedure = createMedicalProcedure(NAME_MEDICAL_PROCEDURE, PRICE, DESCRIPTION_MEDICAL_PROCEDURE);
    this.doctor = createDoctor(NAME_DOCTOR, TEST_DOCTOR_CNP, SEX, SPECIALIZATION, PHONE_NUMBER);
  }

  protected void clear() {
    patientRepository.deleteAll();
    doctorRepository.deleteAll();
    medicalProcedureRepository.deleteAll();
  }

  protected Patient createPatient(
      String firstName,
      String lastName,
      LocalDate birthDate,
      String cnp,
      Sex sex,
      String city,
      String phoneNumber) {

    return patientRepository
        .findByCnp(cnp)
        .orElseGet(
            () ->
                patientRepository.save(
                    new Patient(firstName, lastName, birthDate, cnp, sex, city, phoneNumber)));
  }

  protected Patient createTestPatient(String cnp) {
    return patientRepository
        .findByCnp(cnp)
        .orElseGet(
            () ->
                patientRepository.save(
                    new Patient(
                        FIRST_NAME_PATIENT,
                        LAST_NAME_PATIENT,
                        BIRTH_DATE,
                        cnp,
                        SEX,
                        CITY,
                        PHONE_NUMBER)));
  }

  protected String createPatientUri(String uri) {
    return "http://localhost:" + port + uri;
  }

  protected Doctor createDoctor(
      String name, String cnp, Sex sex, String specialization, String phoneNumber) {

    Doctor doctor =
        doctorRepository
            .findByCnp(cnp)
            .orElseGet(
                () ->
                    doctorRepository.save(new Doctor(name, cnp, sex, specialization, phoneNumber)));
    doctor.getMedicalProcedures().add(medicalProcedure);
    doctorRepository.save(doctor);

    return doctor;
  }

  protected MedicalProcedure createMedicalProcedure(String name, Double price, String description) {
    return medicalProcedureRepository.save(new MedicalProcedure(name, price, description));
  }

  protected Doctor createTestDoctor(String cnp) {
    return doctorRepository
        .findByCnp(cnp)
        .orElseGet(
            () ->
                doctorRepository.save(
                    new Doctor(NAME_DOCTOR, cnp, SEX, SPECIALIZATION, PHONE_NUMBER)));
  }

  public Doctor getTestDoctor() {
    return doctor;
  }

  protected Patient getTestPatient() {
    return patient;
  }

  public MedicalProcedure getTestMedicalProcedure() {
    return medicalProcedure;
  }
}
