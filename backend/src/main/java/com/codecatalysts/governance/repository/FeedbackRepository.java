package com.codecatalysts.governance.repository;

import com.codecatalysts.governance.entity.Application;
import com.codecatalysts.governance.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findByApplication(Application application);
}

