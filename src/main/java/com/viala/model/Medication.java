package com.viala.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "medications")
@Data
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Unit unit;

    @NotNull
    @Future
    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private MedicationList list;
}
