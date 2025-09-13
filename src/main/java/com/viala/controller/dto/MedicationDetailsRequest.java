package com.viala.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MedicationDetailsRequest {
    @NotNull
    private Long medicationListId;
    @NotNull
    private Long medicationId;
    @PositiveOrZero
    private BigDecimal quantity;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "expirationDate must be today or in the future")
    private LocalDate expirationDate;
}
