package com.kronsoft.medicaloffice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalProcedureDTO {
  private Long id;

  private String name;

  private Double price;

  private String description;
}
