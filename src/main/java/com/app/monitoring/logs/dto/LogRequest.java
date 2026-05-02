package com.app.monitoring.logs.dto;

import com.app.monitoring.logs.LogLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

@Builder
public record LogRequest(
        @NotNull LogLevel level,
        @NotBlank String message,
        @NotBlank String serviceName,
        @NotNull Instant timestamp
) {
}
