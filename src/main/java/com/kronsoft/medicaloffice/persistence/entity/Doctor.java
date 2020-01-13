package com.kronsoft.medicaloffice.persistence.entity;

import com.kronsoft.medicaloffice.persistence.entity.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @NotBlank
  @Column(name = "name", nullable = false)
  private String name;

  @NotBlank
  @Size(min = 13, max = 13)
  @Column(name = "cnp", nullable = false, length = 13)
  private String cnp;

  @NotNull
  @Column(name = "sex", nullable = false)
  @Enumerated(EnumType.STRING)
  private Sex sex;

  @Column(name = "specialization", length = 50)
  private String specialization;

  @NotBlank
  @Size(max = 12)
  @Column(name = "phone_number", length = 12)
  private String phoneNumber;

  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(
          name = "doctor_medical_procedure",
          joinColumns = {@JoinColumn(name = "doctor_id")},
          inverseJoinColumns = {@JoinColumn(name = "medical_procedure_id")})
  private Set<MedicalProcedure> medicalProcedures = new HashSet<>();

  public Doctor(
      @NotBlank String name,
      @NotBlank @Size(min = 13, max = 13) String cnp,
      @NotNull Sex sex,
      String specialization,
      @NotBlank @Size(max = 12) String phoneNumber) {
    this.name = name;
    this.cnp = cnp;
    this.sex = sex;
    this.specialization = specialization;
    this.phoneNumber = phoneNumber;
  }
}
