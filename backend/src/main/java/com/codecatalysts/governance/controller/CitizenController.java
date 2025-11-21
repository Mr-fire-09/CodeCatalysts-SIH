package com.codecatalysts.governance.controller;

import com.codecatalysts.governance.dto.ApiResponse;
import com.codecatalysts.governance.dto.ApplicationRequest;
import com.codecatalysts.governance.entity.Application;
import com.codecatalysts.governance.service.ApplicationService;
import com.codecatalysts.governance.service.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citizen")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CITIZEN')")
public class CitizenController {

    private final ApplicationService applicationService;
    private final CurrentUserService currentUserService;

    @PostMapping("/applications")
    public ResponseEntity<ApiResponse<Application>> submit(@Valid @RequestBody ApplicationRequest request) {
        var user = currentUserService.getCurrentUser();
        Application application = applicationService.submitApplication(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.<Application>builder()
                .success(true)
                .message("Application submitted")
                .data(application)
                .build());
    }

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<List<Application>>> myApplications() {
        var user = currentUserService.getCurrentUser();
        List<Application> list = applicationService.getCitizenApplications(user.getId());
        return ResponseEntity.ok(ApiResponse.<List<Application>>builder()
                .success(true)
                .message("Fetched applications")
                .data(list)
                .build());
    }

    @GetMapping("/applications/{trackingId}")
    public ResponseEntity<ApiResponse<Application>> track(@PathVariable String trackingId) {
        Application application = applicationService.getByTrackingId(trackingId);
        return ResponseEntity.ok(ApiResponse.<Application>builder()
                .success(true)
                .message("Application retrieved")
                .data(application)
                .build());
    }

    @GetMapping("/applications/{trackingId}/timeline")
    public ResponseEntity<ApiResponse<?>> timeline(@PathVariable String trackingId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Timeline retrieved")
                .data(applicationService.getTimeline(trackingId))
                .build());
    }
}

