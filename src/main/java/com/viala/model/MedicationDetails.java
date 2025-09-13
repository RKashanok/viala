package com.viala.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "medication_details")
@Data
public class MedicationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medication_id", nullable = false)
    @NotNull
    private Medication medication;

    @Column(name = "quantity", precision = 19, scale = 2)
    @PositiveOrZero
    private BigDecimal quantity;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medication_list_id", nullable = false)
    @NotNull
    @JsonBackReference
    private MedicationList medicationList;
}