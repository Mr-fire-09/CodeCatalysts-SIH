package com.codecatalysts.governance.controller;

import com.codecatalysts.governance.dto.ApiResponse;
import com.codecatalysts.governance.dto.AssignApplicationRequest;
import com.codecatalysts.governance.dto.AutoApprovalConfigRequest;
import com.codecatalysts.governance.entity.Application;
import com.codecatalysts.governance.entity.ApplicationStatus;
import com.codecatalysts.governance.repository.ApplicationRepository;
import com.codecatalysts.governance.service.ApplicationService;
import com.codecatalysts.governance.service.DashboardService;
import com.codecatalysts.governance.service.PlatformSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;
    private final DashboardService dashboardService;
    private final PlatformSettingsService settingsService;

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<List<Application>>> allApplications() {
        List<Application> applications = applicationRepository.findAll();
        return ResponseEntity.ok(ApiResponse.<List<Application>>builder()
                .success(true)
                .message("All applications")
                .data(applications)
                .build());
    }

    @PostMapping("/applications/{trackingId}/assign")
    public ResponseEntity<ApiResponse<Application>> assign(
            @PathVariable String trackingId,
            @Valid @RequestBody AssignApplicationRequest request) {
        Application application = applicationService.assignApplication(trackingId, request);
        return ResponseEntity.ok(ApiResponse.<Application>builder()
                .success(true)
                .message("Application assigned")
                .data(application)
                .build());
    }

    @GetMapping("/delays")
    public ResponseEntity<ApiResponse<List<Application>>> delayedApplications() {
        List<Application> delayed = applicationRepository
                .findByStatusAndUpdatedAtBefore(ApplicationStatus.IN_REVIEW, LocalDateTime.now().minusHours(settingsService.getDelayThresholdHours()));
        return ResponseEntity.ok(ApiResponse.<List<Application>>builder()
                .success(true)
                .message("Delayed applications")
                .data(delayed)
                .build());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<?>> dashboard() {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Dashboard summary")
                .data(dashboardService.summarize())
                .build());
    }

    @PostMapping("/settings/auto-approval")
    public ResponseEntity<ApiResponse<?>> configure(@Valid @RequestBody AutoApprovalConfigRequest request) {
        settingsService.update(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Settings updated")
                .build());
    }
}

