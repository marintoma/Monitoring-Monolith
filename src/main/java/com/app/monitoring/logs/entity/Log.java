package com.app.monitoring.logs.entity;

import com.app.monitoring.logs.LogLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "logs")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogLevel level;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @NotBlank
    @Column(nullable = false)
    @Size(max = 100)
    private String serviceName;

    @NotNull
    @Column(nullable = false)
    private Instant timestamp;
}
