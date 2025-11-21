package com.codecatalysts.governance.service;

import com.codecatalysts.governance.dto.AutoApprovalConfigRequest;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class PlatformSettingsService {

    @Getter
    private int autoApproveDays = 30;

    @Getter
    private int delayThresholdHours = 24;

    public synchronized void update(AutoApprovalConfigRequest request) {
        this.autoApproveDays = request.getAutoApproveDays();
        this.delayThresholdHours = request.getDelayThresholdHours();
    }
}

