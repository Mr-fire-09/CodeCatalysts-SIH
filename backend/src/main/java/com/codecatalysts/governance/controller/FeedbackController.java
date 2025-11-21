package com.codecatalysts.governance.controller;

import com.codecatalysts.governance.dto.ApiResponse;
import com.codecatalysts.governance.dto.FeedbackRequest;
import com.codecatalysts.governance.service.CurrentUserService;
import com.codecatalysts.governance.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final CurrentUserService currentUserService;

    @PostMapping("/{trackingId}/otp")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<ApiResponse<?>> issueOtp(@PathVariable String trackingId) {
        var user = currentUserService.getCurrentUser();
        feedbackService.initiateFeedback(trackingId, user);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("OTP sent to registered channel")
                .build());
    }

    @PostMapping("/{trackingId}")
    @PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<ApiResponse<?>> submit(
            @PathVariable String trackingId,
            @Valid @RequestBody FeedbackRequest request) {
        var user = currentUserService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Feedback submitted")
                .data(feedbackService.submitFeedback(trackingId, request, user))
                .build());
    }
}

