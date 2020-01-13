package com.kronsoft.medicaloffice.dto;

import com.kronsoft.medicaloffice.persistence.entity.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientContentDTO {

  @NotBlank private String firstName;

  @NotBlank private String lastName;

  private LocalDate birthDate;

  @NotBlank
  @Size(min = 13, max = 13)
  private String cnp;

  @NotNull private Sex sex;

  private String city;

  @NotBlank
  @Size(max = 12)
  private String phoneNumber;

}
