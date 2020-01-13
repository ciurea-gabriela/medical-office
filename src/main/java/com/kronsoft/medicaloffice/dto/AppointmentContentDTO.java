package com.kronsoft.medicaloffice.dto;

import com.kronsoft.medicaloffice.persistence.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentContentDTO {

  @NotNull private Status status;

  @NotNull private LocalDateTime startTime;

  @NotNull private LocalDateTime endTime;

  private String description;

  @NotNull private Long doctorId;

  @NotNull private Long medicalProcedureId;
}
