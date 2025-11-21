package com.codecatalysts.governance.service;

import com.codecatalysts.governance.entity.Application;
import com.codecatalysts.governance.entity.ApplicationStatus;
import com.codecatalysts.governance.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ApplicationRepository applicationRepository;

    public Map<String, Object> summarize() {
        List<Application> applications = applicationRepository.findAll();
        Map<ApplicationStatus, Long> statusBreakdown = new EnumMap<>(ApplicationStatus.class);
        for (ApplicationStatus status : ApplicationStatus.values()) {
            statusBreakdown.put(status, applications.stream().filter(app -> app.getStatus() == status).count());
        }

        DoubleSummaryStatistics ratingStats = applications.stream()
                .filter(app -> app.getAverageRating() != null)
                .mapToDouble(Application::getAverageRating)
                .summaryStatistics();

        long delayed = applications.stream()
                .filter(app -> app.getUpdatedAt().isBefore(LocalDateTime.now().minusHours(24)))
                .count();

        return Map.of(
                "totalApplications", applications.size(),
                "statusBreakdown", statusBreakdown,
                "averageRating", ratingStats.getAverage(),
                "delayedApplications", delayed
        );
    }
}

