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
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @NotBlank
  @Column(name = "first_name", nullable = false)
  private String firstName;

  @NotBlank
  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @NotBlank
  @Size(min = 13, max = 13)
  @Column(name = "cnp", nullable = false, length = 13)
  private String cnp;

  @NotNull
  @Column(name = "sex", nullable = false)
  @Enumerated(EnumType.STRING)
  private Sex sex;

  @Column(name = "city", length = 50)
  private String city;

  @NotBlank
  @Size(max = 12)
  @Column(name = "phone_number", nullable = false, length = 12)
  private String phoneNumber;

  @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Appointment> appointments;

  public Patient(
      @NotBlank String firstName,
      @NotBlank String lastName,
      LocalDate birthDate,
      @NotBlank @Size(min = 13, max = 13) String cnp,
      @NotNull Sex sex,
      String city,
      @NotBlank @Size(max = 12) String phoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDate = birthDate;
    this.cnp = cnp;
    this.sex = sex;
    this.city = city;
    this.phoneNumber = phoneNumber;
  }
}
