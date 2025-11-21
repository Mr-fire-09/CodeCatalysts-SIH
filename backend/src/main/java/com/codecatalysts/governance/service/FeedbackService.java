package com.codecatalysts.governance.service;

import com.codecatalysts.governance.dto.FeedbackRequest;
import com.codecatalysts.governance.entity.Application;
import com.codecatalysts.governance.entity.Feedback;
import com.codecatalysts.governance.entity.User;
import com.codecatalysts.governance.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ApplicationService applicationService;
    private final OtpService otpService;

    public void initiateFeedback(String trackingId, User citizen) {
        Application application = applicationService.getByTrackingId(trackingId);
        otpService.issueOtp(application, citizen);
    }

    @Transactional
    public Feedback submitFeedback(String trackingId, FeedbackRequest request, User citizen) {
        Application application = applicationService.getByTrackingId(trackingId);
        boolean verified = otpService.verifyOtp(application, request.getOtpCode());
        if (!verified) {
            throw new IllegalArgumentException("OTP verification failed");
        }

        Feedback feedback = Feedback.builder()
                .application(application)
                .citizen(citizen)
                .rating(request.getRating())
                .comment(request.getComment())
                .otpVerified(true)
                .build();

        Feedback saved = feedbackRepository.save(feedback);
        updateApplicationRating(application);
        return saved;
    }

    public List<Feedback> getApplicationFeedback(String trackingId) {
        Application application = applicationService.getByTrackingId(trackingId);
        return feedbackRepository.findByApplication(application);
    }

    private void updateApplicationRating(Application application) {
        List<Feedback> feedbackList = feedbackRepository.findByApplication(application);
        DoubleSummaryStatistics stats = feedbackList.stream()
                .mapToDouble(Feedback::getRating)
                .summaryStatistics();
        application.setAverageRating(stats.getAverage());
    }
}

