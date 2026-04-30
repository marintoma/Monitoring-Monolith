package com.app.monitoring.metrics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

@Builder
public record MetricRequest(
        @NotBlank String name,
        @NotNull Double value,
        @NotBlank String serviceName,
        @NotNull Instant timestamp
) {
}
