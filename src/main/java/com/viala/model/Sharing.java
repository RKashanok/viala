package com.viala.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sharing")
@Data
public class Sharing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medication_list_id")
    private MedicationList medicationList;

    @ManyToOne
    @JoinColumn(name = "shared_with_id")
    private User sharedWith;

    @Enumerated(EnumType.STRING)
    private Permission permission;
}
