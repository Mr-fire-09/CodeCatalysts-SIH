package com.codecatalysts.governance.repository;

import com.codecatalysts.governance.entity.Application;
import com.codecatalysts.governance.entity.ApplicationStatus;
import com.codecatalysts.governance.entity.Official;
import com.codecatalysts.governance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    Optional<Application> findByTrackingId(String trackingId);
    List<Application> findByCitizen(User citizen);
    List<Application> findByAssignedOfficial(Official official);
    List<Application> findByStatusAndUpdatedAtBefore(ApplicationStatus status, LocalDateTime updatedAt);
}

