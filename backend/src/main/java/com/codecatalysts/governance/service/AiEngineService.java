package com.codecatalysts.governance.service;

import com.codecatalysts.governance.entity.Application;
import com.codecatalysts.governance.entity.ApplicationStatus;
import com.codecatalysts.governance.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiEngineService {

    private final ApplicationRepository applicationRepository;
    private final DelayDetectorService delayDetectorService;
    private final NotificationService notificationService;
    private final BlockchainService blockchainService;
    private final PlatformSettingsService settingsService;

    public Application analyze(Application application) {
        double score = delayDetectorService.calculateDelayScore(application);
        application.setAiDelayScore(score);
        application.setAiFlagged(score >= 0.8);
        if (delayDetectorService.isDelayed(application)) {
            notificationService.notifyOfficial(application, "AI detected inactivity on " + application.getTrackingId());
            notificationService.notifyCitizen(application.getCitizen(), "Your application is experiencing a delay. Officials have been notified.");
        }
        return applicationRepository.save(application);
    }

    @Scheduled(fixedDelayString = "${app.ai.monitor-interval-ms:60000}")
    public void monitorAll() {
        List<Application> applications = applicationRepository.findAll();
        applications.forEach(app -> {
            analyze(app);
            autoApproveIfRequired(app);
        });
    }

    private void autoApproveIfRequired(Application app) {
        if (app.getStatus() == ApplicationStatus.APPROVED || app.getStatus() == ApplicationStatus.REJECTED) {
            return;
        }
        int autoApproveDays = settingsService.getAutoApproveDays();
        if (app.getCreatedAt().isBefore(LocalDateTime.now().minusDays(autoApproveDays))) {
            app.setStatus(ApplicationStatus.AUTO_APPROVED);
            applicationRepository.save(app);
            if (app.getDocumentHash() != null) {
                blockchainService.anchor(app);
            }
            notificationService.notifyCitizen(app.getCitizen(), "Application " + app.getTrackingId() + " auto-approved after " + autoApproveDays + " days.");
        }
    }
}

