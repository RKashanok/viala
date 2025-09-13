package com.viala.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_allergies")
@Data
public class UserAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String allergies;
}
