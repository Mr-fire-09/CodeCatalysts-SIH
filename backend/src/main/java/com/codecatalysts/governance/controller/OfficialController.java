package com.codecatalysts.governance.controller;

import com.codecatalysts.governance.dto.ApiResponse;
import com.codecatalysts.governance.dto.ApplicationStatusUpdateRequest;
import com.codecatalysts.governance.entity.Application;
import com.codecatalysts.governance.entity.User;
import com.codecatalysts.governance.service.ApplicationService;
import com.codecatalysts.governance.service.CurrentUserService;
import com.codecatalysts.governance.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/official")
@PreAuthorize("hasRole('OFFICIAL')")
@RequiredArgsConstructor
public class OfficialController {

    private final ApplicationService applicationService;
    private final CurrentUserService currentUserService;
    private final FeedbackService feedbackService;

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<List<Application>>> assigned() {
        User official = currentUserService.getCurrentUser();
        List<Application> apps = applicationService.getOfficialApplications(official.getId());
        return ResponseEntity.ok(ApiResponse.<List<Application>>builder()
                .success(true)
                .message("Assigned applications")
                .data(apps)
                .build());
    }

    @PostMapping("/applications/{trackingId}/status")
    public ResponseEntity<ApiResponse<Application>> updateStatus(
            @PathVariable String trackingId,
            @RequestBody @Valid ApplicationStatusUpdateRequest request) {
        User official = currentUserService.getCurrentUser();
        Application updated = applicationService.updateStatus(trackingId, request, official);
        return ResponseEntity.ok(ApiResponse.<Application>builder()
                .success(true)
                .message("Status updated")
                .data(updated)
                .build());
    }

    @GetMapping("/applications/{trackingId}/feedback")
    public ResponseEntity<ApiResponse<?>> feedback(@PathVariable String trackingId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Citizen feedback")
                .data(feedbackService.getApplicationFeedback(trackingId))
                .build());
    }
}

