package com.codecatalysts.governance.repository;

import com.codecatalysts.governance.entity.OTPRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OTPRecordRepository extends JpaRepository<OTPRecord, UUID> {
    Optional<OTPRecord> findTopByApplication_IdOrderByCreatedAtDesc(UUID applicationId);
}

