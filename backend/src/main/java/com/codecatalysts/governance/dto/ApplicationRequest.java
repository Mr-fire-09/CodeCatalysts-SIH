package com.codecatalysts.governance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationRequest {

    @NotBlank
    private String description;

    private String documentUrl;

    @NotNull
    private Integer priority;
}

