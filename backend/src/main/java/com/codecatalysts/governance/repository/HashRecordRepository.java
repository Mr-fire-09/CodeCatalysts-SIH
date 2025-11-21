package com.codecatalysts.governance.repository;

import com.codecatalysts.governance.entity.HashRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HashRecordRepository extends JpaRepository<HashRecord, UUID> {
    Optional<HashRecord> findTopByOrderByCreatedAtDesc();
    Optional<HashRecord> findByApplication_Id(UUID applicationId);
}

