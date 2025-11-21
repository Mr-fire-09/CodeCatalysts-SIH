package com.codecatalysts.governance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String trackingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen_id")
    private User citizen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "official_id")
    private Official assignedOfficial;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.NEW;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String documentUrl;

    private String documentHash;

    private LocalDate expectedResolutionDate;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    private LocalDateTime lastCitizenActionAt = LocalDateTime.now();

    private LocalDateTime lastOfficialActionAt = LocalDateTime.now();

    private boolean aiFlagged = false;

    private double aiDelayScore = 0.0;

    private Double averageRating;
}

