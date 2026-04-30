package com.app.monitoring.metrics.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record MetricResponse(
        Long id,
        String name,
        Double value,
        String serviceName,
        Instant timestamp
) {
}
