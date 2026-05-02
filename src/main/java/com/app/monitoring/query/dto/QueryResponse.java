package com.app.monitoring.query.dto;

import com.app.monitoring.common.PageResponse;
import com.app.monitoring.logs.dto.LogResponse;
import com.app.monitoring.metrics.dto.MetricResponse;
import lombok.Builder;

import java.time.Instant;

@Builder
public record QueryResponse(
        String serviceName,
        Instant from,
        Instant to,
        PageResponse<MetricResponse> metrics,
        PageResponse<LogResponse> logs
) {
}
