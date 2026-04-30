package com.app.monitoring.metrics.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "metrics")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Double value;

    @NotBlank
    @Column(nullable = false)
    private String serviceName;

    @NotNull
    @Column(nullable = false)
    private Instant timestamp;


}
