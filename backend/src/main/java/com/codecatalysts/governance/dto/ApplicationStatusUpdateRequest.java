package com.codecatalysts.governance.dto;

import com.codecatalysts.governance.entity.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationStatusUpdateRequest {

    @NotNull
    private ApplicationStatus status;

    @NotBlank
    private String remarks;
}

