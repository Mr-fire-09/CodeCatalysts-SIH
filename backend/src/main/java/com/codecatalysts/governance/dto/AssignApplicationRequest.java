package com.codecatalysts.governance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignApplicationRequest {

    @NotBlank
    private String officialId;
}

