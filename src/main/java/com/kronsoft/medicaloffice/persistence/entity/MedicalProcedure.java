package com.kronsoft.medicaloffice.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "medical_procedure")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalProcedure {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @NotBlank
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "price", nullable = false)
  private Double price;

  @Column(name = "description", columnDefinition="TEXT")
  private String description;

  public MedicalProcedure(@NotBlank String name, @NotNull Double price, String description) {
    this.name = name;
    this.price = price;
    this.description = description;
  }
}
