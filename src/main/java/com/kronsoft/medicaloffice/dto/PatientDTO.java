package com.kronsoft.medicaloffice.dto;

import com.kronsoft.medicaloffice.persistence.entity.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
  private Long id;

  private String firstName;

  private String lastName;

  private LocalDate birthDate;

  private String cnp;

  private Sex sex;

  private String city;

  private String phoneNumber;
}
