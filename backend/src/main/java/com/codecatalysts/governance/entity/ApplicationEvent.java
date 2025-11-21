package com.codecatalysts.governance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_events")
public class ApplicationEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private Application application;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private String actor;

    private LocalDateTime timestamp = LocalDateTime.now();
}

