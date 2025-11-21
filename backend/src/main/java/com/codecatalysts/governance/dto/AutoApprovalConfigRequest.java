package com.codecatalysts.governance.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AutoApprovalConfigRequest {

    @NotNull
    @Min(1)
    @Max(60)
    private Integer autoApproveDays;

    @NotNull
    @Min(1)
    @Max(72)
    private Integer delayThresholdHours;
}

