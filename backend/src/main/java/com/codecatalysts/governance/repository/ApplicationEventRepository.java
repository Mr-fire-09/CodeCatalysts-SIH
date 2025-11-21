package com.codecatalysts.governance.repository;

import com.codecatalysts.governance.entity.Application;
import com.codecatalysts.governance.entity.ApplicationEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationEventRepository extends JpaRepository<ApplicationEvent, UUID> {
    List<ApplicationEvent> findByApplicationOrderByTimestampAsc(Application application);
}

