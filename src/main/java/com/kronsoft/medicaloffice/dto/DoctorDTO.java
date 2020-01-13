package com.kronsoft.medicaloffice.dto;

import com.kronsoft.medicaloffice.persistence.entity.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
  private Long id;

  private String name;

  private String cnp;

  private Sex sex;

  private String specialization;

  private String phoneNumber;
}
