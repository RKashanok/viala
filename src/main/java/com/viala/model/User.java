package com.viala.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fullName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Min(0)
    @Max(120)
    private Integer age;

    @ElementCollection
    private List<String> allergies;
    
    @NotBlank
    private String password;
}
