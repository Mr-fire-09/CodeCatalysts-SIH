package com.codecatalysts.governance.repository;

import com.codecatalysts.governance.entity.Official;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OfficialRepository extends JpaRepository<Official, UUID> {
    List<Official> findByAvailableTrue();
}

