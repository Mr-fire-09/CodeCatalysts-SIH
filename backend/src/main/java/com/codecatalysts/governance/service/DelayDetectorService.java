package com.codecatalysts.governance.service;

import com.codecatalysts.governance.entity.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DelayDetectorService {

    private final PlatformSettingsService settingsService;

    public double calculateDelayScore(Application application) {
        long thresholdHours = settingsService.getDelayThresholdHours();
        Duration sinceUpdate = Duration.between(application.getUpdatedAt(), LocalDateTime.now());
        double score = sinceUpdate.toHours() / (double) thresholdHours;
        return Math.min(1.5, Math.max(0.0, score));
    }

    public boolean isDelayed(Application application) {
        return calculateDelayScore(application) >= 1.0;
    }
}

