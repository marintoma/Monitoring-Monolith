package com.app.monitoring.logs.dto;

import com.app.monitoring.logs.LogLevel;
import lombok.Builder;

import java.time.Instant;

@Builder
public record LogResponse(
    Long id,
    LogLevel level,
    String message,
    String serviceName,
    Instant timestamp
) {
}
