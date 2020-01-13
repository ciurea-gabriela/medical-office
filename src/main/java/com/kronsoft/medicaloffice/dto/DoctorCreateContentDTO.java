package com.kronsoft.medicaloffice.dto;

import com.kronsoft.medicaloffice.persistence.entity.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorCreateContentDTO {
  @NotBlank private String name;

  @NotBlank
  @Size(min = 13, max = 13)
  private String cnp;

  @NotNull private Sex sex;

  private String specialization;

  @NotBlank
  @Size(max = 12)
  private String phoneNumber;

  @NotNull private Long medicalProcedureId;
}
