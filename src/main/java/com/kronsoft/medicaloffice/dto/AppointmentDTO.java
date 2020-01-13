package com.kronsoft.medicaloffice.dto;

import com.kronsoft.medicaloffice.persistence.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
  private Long id;

  private Status status;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  private String description;

  private Long doctorId;

  private String doctorName;

  private Long medicalProcedureId;

  private String medicalProcedureName;
}
