package com.codecatalysts.governance.service;

import com.codecatalysts.governance.entity.Application;
import com.codecatalysts.governance.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void notifyCitizen(User citizen, String message) {
        log.info("[Notification] Citizen {} - {}", citizen.getEmail(), message);
    }

    public void notifyOfficial(Application application, String message) {
        if (application.getAssignedOfficial() != null) {
            log.info("[Notification] Official {} - {}", application.getAssignedOfficial().getUser().getEmail(), message);
        }
    }

    public void notifyAdmin(String message) {
        log.info("[Notification] Admin - {}", message);
    }
}

