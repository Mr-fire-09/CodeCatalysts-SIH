package com.codecatalysts.governance.service;

import com.codecatalysts.governance.dto.ApplicationRequest;
import com.codecatalysts.governance.dto.ApplicationStatusUpdateRequest;
import com.codecatalysts.governance.dto.AssignApplicationRequest;
import com.codecatalysts.governance.entity.*;
import com.codecatalysts.governance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final OfficialRepository officialRepository;
    private final ApplicationEventRepository eventRepository;
    private final AiEngineService aiEngineService;
    private final NotificationService notificationService;
    private final BlockchainService blockchainService;

    @Transactional
    public Application submitApplication(UUID citizenId, ApplicationRequest request) {
        User citizen = userRepository.findById(citizenId).orElseThrow();
        Application application = Application.builder()
                .trackingId(generateTrackingId())
                .citizen(citizen)
                .description(request.getDescription())
                .documentUrl(request.getDocumentUrl())
                .documentHash(hashDocument(request.getDescription(), request.getDocumentUrl()))
                .expectedResolutionDate(LocalDate.now().plusDays(15))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastCitizenActionAt(LocalDateTime.now())
                .build();
        Application saved = applicationRepository.save(application);
        recordEvent(saved, ApplicationStatus.NEW.name(), "Application submitted", citizen.getFullName());
        aiEngineService.analyze(saved);
        notificationService.notifyCitizen(citizen, "Application submitted successfully. Tracking ID: " + saved.getTrackingId());
        return saved;
    }

    @Transactional
    public Application assignApplication(String trackingId, AssignApplicationRequest request) {
        Application application = getByTrackingId(trackingId);
        Official official = officialRepository.findById(UUID.fromString(request.getOfficialId())).orElseThrow();
        application.setAssignedOfficial(official);
        application.setStatus(ApplicationStatus.IN_REVIEW);
        application.setUpdatedAt(LocalDateTime.now());
        Application updated = applicationRepository.save(application);
        recordEvent(updated, ApplicationStatus.IN_REVIEW.name(), "Assigned to " + official.getUser().getFullName(), "Admin");
        notificationService.notifyOfficial(updated, "New application assigned: " + updated.getTrackingId());
        return updated;
    }

    @Transactional
    public Application updateStatus(String trackingId, ApplicationStatusUpdateRequest request, User actor) {
        Application application = getByTrackingId(trackingId);
        application.setStatus(request.getStatus());
        application.setUpdatedAt(LocalDateTime.now());
        application.setLastOfficialActionAt(LocalDateTime.now());
        Application updated = applicationRepository.save(application);
        recordEvent(updated, request.getStatus().name(), request.getRemarks(), actor.getFullName());
        notificationService.notifyCitizen(application.getCitizen(), "Application " + trackingId + " updated to " + request.getStatus());
        if (request.getStatus() == ApplicationStatus.APPROVED && application.getDocumentHash() != null) {
            blockchainService.anchor(application);
        }
        return aiEngineService.analyze(updated);
    }

    public Application getByTrackingId(String trackingId) {
        return applicationRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
    }

    public List<Application> getCitizenApplications(UUID citizenId) {
        User citizen = userRepository.findById(citizenId).orElseThrow();
        return applicationRepository.findByCitizen(citizen);
    }

    public List<Application> getOfficialApplications(UUID officialUserId) {
        User user = userRepository.findById(officialUserId).orElseThrow();
        Official official = officialRepository.findAll().stream()
                .filter(o -> o.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow();
        return applicationRepository.findByAssignedOfficial(official);
    }

    public List<ApplicationEvent> getTimeline(String trackingId) {
        Application application = getByTrackingId(trackingId);
        return eventRepository.findByApplicationOrderByTimestampAsc(application);
    }

    private void recordEvent(Application application, String status, String remarks, String actor) {
        ApplicationEvent event = ApplicationEvent.builder()
                .application(application)
                .status(status)
                .remarks(remarks)
                .actor(actor)
                .timestamp(LocalDateTime.now())
                .build();
        eventRepository.save(event);
    }

    private String generateTrackingId() {
        return "DG-" + System.currentTimeMillis();
    }

    private String hashDocument(String description, String documentUrl) {
        String payload = description + "|" + documentUrl + "|" + LocalDateTime.now();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String hexString = Integer.toHexString(0xff & b);
                if (hexString.length() == 1) {
                    hex.append('0');
                }
                hex.append(hexString);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}

