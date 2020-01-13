package com.kronsoft.medicaloffice.dto;

import com.kronsoft.medicaloffice.persistence.entity.enums.UpdateOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalProceduresWithinDoctorDTO {
  @NotNull private Long medicalProcedureId;

  @NotNull private UpdateOperation updateOperation;
}
