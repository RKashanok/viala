package com.viala.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class SymptomRequest {
    private String symptomDescription;
    private List<String> currentMedications;
}
