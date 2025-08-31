package com.viala.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "medication_lists")
@Data
public class MedicationList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
