package com.kronsoft.medicaloffice.persistence.entity;

import com.kronsoft.medicaloffice.persistence.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @NotNull
  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;

  @NotNull
  @Column(name = "start_time", nullable = false)
  private LocalDateTime startTime;

  @NotNull
  @Column(name = "end_time", nullable = false)
  private LocalDateTime endTime;

  @Column(name = "description")
  private String description;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "patient_id")
  private Patient patient;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "doctor_id")
  private Doctor doctor;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "medical_procedure_id")
  private MedicalProcedure medicalProcedure;


  public Appointment(
      @NotNull Status status,
      @NotNull LocalDateTime startTime,
      @NotNull LocalDateTime endTime,
      String description,
      Patient patient,
      Doctor doctor,
      MedicalProcedure medicalProcedure) {
    this.status = status;
    this.startTime = startTime;
    this.endTime = endTime;
    this.description = description;
    this.patient = patient;
    this.doctor = doctor;
    this.medicalProcedure = medicalProcedure;
  }
}
