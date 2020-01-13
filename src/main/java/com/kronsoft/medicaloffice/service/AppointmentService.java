package com.kronsoft.medicaloffice.service;

import com.kronsoft.medicaloffice.dto.AppointmentContentDTO;
import com.kronsoft.medicaloffice.dto.AppointmentDTO;
import com.kronsoft.medicaloffice.dto.AppointmentUpdateDTO;
import com.kronsoft.medicaloffice.dto.AppointmentWithPatientInfoDTO;
import com.kronsoft.medicaloffice.exception.InvalidInputException;
import com.kronsoft.medicaloffice.exception.ResourceNotFoundException;
import com.kronsoft.medicaloffice.persistence.entity.Appointment;
import com.kronsoft.medicaloffice.persistence.entity.Doctor;
import com.kronsoft.medicaloffice.persistence.entity.MedicalProcedure;
import com.kronsoft.medicaloffice.persistence.entity.Patient;
import com.kronsoft.medicaloffice.persistence.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {
  private PatientService patientService;
  private AppointmentRepository appointmentRepository;
  private DoctorService doctorService;

  @Autowired
  public AppointmentService(
      PatientService patientService,
      AppointmentRepository appointmentRepository,
      DoctorService doctorService) {
    this.patientService = patientService;
    this.appointmentRepository = appointmentRepository;
    this.doctorService = doctorService;
  }

  public AppointmentDTO createAppointment(AppointmentContentDTO appointmentContent, Long patientId)
      throws ResourceNotFoundException, InvalidInputException {
    Patient patient = patientService.findById(patientId);

    Doctor doctor = doctorService.findById(appointmentContent.getDoctorId());
    MedicalProcedure medicalProcedure =
        getMedicalProcedure(doctor, appointmentContent.getMedicalProcedureId());

    Appointment appointment =
        appointmentRepository.save(
            new Appointment(
                appointmentContent.getStatus(),
                appointmentContent.getStartTime(),
                appointmentContent.getEndTime(),
                appointmentContent.getDescription(),
                patient,
                doctor,
                medicalProcedure));

    patient.getAppointments().add(appointment);
    patientService.save(patient);

    return new AppointmentDTO(
        appointment.getId(),
        appointment.getStatus(),
        appointment.getStartTime(),
        appointment.getEndTime(),
        appointment.getDescription(),
        appointment.getDoctor().getId(),
        appointment.getDoctor().getName(),
        appointment.getMedicalProcedure().getId(),
        appointment.getMedicalProcedure().getName());
  }

  public AppointmentDTO getAppointment(Long patientId, Long appointmentId)
      throws ResourceNotFoundException {
    Appointment appointment =
        appointmentRepository
            .findByIdAndPatientId(appointmentId, patientId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Appointment not found for patient id: " + patientId));

    return new AppointmentDTO(
        appointment.getId(),
        appointment.getStatus(),
        appointment.getStartTime(),
        appointment.getEndTime(),
        appointment.getDescription(),
        appointment.getDoctor().getId(),
        appointment.getDoctor().getName(),
        appointment.getMedicalProcedure().getId(),
        appointment.getMedicalProcedure().getName());
  }

  public List<AppointmentDTO> getAllAppointments(Long patientId) {
    List<Appointment> appointments =
        appointmentRepository.findByPatientId(patientId).orElseGet(ArrayList::new);

    List<AppointmentDTO> appointmentDTOList = new ArrayList<>();

    for (Appointment appointment : appointments) {
      appointmentDTOList.add(
          new AppointmentDTO(
              appointment.getId(),
              appointment.getStatus(),
              appointment.getStartTime(),
              appointment.getEndTime(),
              appointment.getDescription(),
              appointment.getDoctor().getId(),
              appointment.getDoctor().getName(),
              appointment.getMedicalProcedure().getId(),
              appointment.getMedicalProcedure().getName()));
    }

    return appointmentDTOList;
  }

  public List<AppointmentWithPatientInfoDTO> getAllAppointmentsWithPatientInfo() {
    List<Appointment> appointments = appointmentRepository.findAll();
    List<AppointmentWithPatientInfoDTO> appointmentsDTO = new ArrayList<>();

    for (Appointment appointment : appointments) {
      String patientName =
          appointment.getPatient().getFirstName() + ' ' + appointment.getPatient().getLastName();
      appointmentsDTO.add(
          new AppointmentWithPatientInfoDTO(
              appointment.getId(),
              appointment.getStatus(),
              appointment.getStartTime(),
              appointment.getEndTime(),
              appointment.getDescription(),
              appointment.getPatient().getId(),
              patientName,
              appointment.getDoctor().getId(),
              appointment.getDoctor().getName(),
              appointment.getMedicalProcedure().getId(),
              appointment.getMedicalProcedure().getName()));
    }

    return appointmentsDTO;
  }

  public void updateAppointmentFromPatient(
      AppointmentContentDTO appointmentContent, Long patientId, Long appointmentId)
      throws ResourceNotFoundException {

    Appointment appointment =
        appointmentRepository
            .findByIdAndPatientId(appointmentId, patientId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Appointment not found for patient id: " + patientId));
    Doctor doctor = doctorService.findById(appointmentContent.getDoctorId());
    MedicalProcedure medicalProcedure =
        getMedicalProcedure(doctor, appointmentContent.getMedicalProcedureId());

    appointment.setStatus(appointmentContent.getStatus());
    appointment.setStartTime(appointmentContent.getStartTime());
    appointment.setEndTime(appointmentContent.getEndTime());
    appointment.setDescription(appointmentContent.getDescription());
    appointment.setDoctor(doctor);
    appointment.setMedicalProcedure(medicalProcedure);

    appointmentRepository.save(appointment);
  }

  public void updateAppointment(Long appointmentId, AppointmentUpdateDTO appointmentUpdateDTO)
      throws ResourceNotFoundException {
    Appointment appointment =
        appointmentRepository
            .findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
    Doctor doctor = doctorService.findById(appointmentUpdateDTO.getDoctorId());
    MedicalProcedure medicalProcedure =
        getMedicalProcedure(doctor, appointmentUpdateDTO.getMedicalProcedureId());

    Patient patient = patientService.findById(appointmentUpdateDTO.getPatientId());

    appointment.setStatus(appointmentUpdateDTO.getStatus());
    appointment.setStartTime(appointmentUpdateDTO.getStartTime());
    appointment.setEndTime(appointmentUpdateDTO.getEndTime());
    appointment.setDescription(appointmentUpdateDTO.getDescription());
    appointment.setDoctor(doctor);
    appointment.setMedicalProcedure(medicalProcedure);
    appointment.setPatient(patient);

    appointmentRepository.save(appointment);
  }

  public void deleteAppointment(Long patientId, Long appointmentId) {
    this.appointmentRepository
        .findByIdAndPatientId(appointmentId, patientId)
        .ifPresent(ap -> appointmentRepository.delete(ap));
  }

  private MedicalProcedure getMedicalProcedure(Doctor doctor, Long medicalProcedureId)
      throws InvalidInputException {
    MedicalProcedure medicalProcedure = null;

    for (MedicalProcedure medProcedure : doctor.getMedicalProcedures()) {
      if (medProcedure.getId().equals(medicalProcedureId)) {
        medicalProcedure = medProcedure;
      }
    }
    if (medicalProcedure != null) {
      return medicalProcedure;
    } else {
      throw new InvalidInputException("Invalid Input");
    }
  }
}
