package com.app.monitoring.notifications.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_rule_id", nullable = false)
    private AlertRule alertRule;

    @NotNull
    @Column(nullable = false)
    private Double triggerValue;

    @NotNull
    @Column(nullable = false)
    private Instant firedAt;
}
