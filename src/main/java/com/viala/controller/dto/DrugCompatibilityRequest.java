package com.viala.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class DrugCompatibilityRequest {
    private List<String> drugNames;
}
