package com.kronsoft.medicaloffice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalProcedureContentDTO {

  @NotBlank private String name;

  @NotNull private Double price;

  private String description;
}
