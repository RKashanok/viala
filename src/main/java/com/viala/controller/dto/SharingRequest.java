package com.viala.controller.dto;

import com.viala.model.Permission;
import lombok.Data;

@Data
public class SharingRequest {
    private String email;
    private Permission permission;
    private Long medicationListId;
}
