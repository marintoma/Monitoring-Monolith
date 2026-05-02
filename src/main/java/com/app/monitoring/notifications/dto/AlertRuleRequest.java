package com.app.monitoring.notifications.dto;

import com.app.monitoring.notifications.ThresholdDirection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AlertRuleRequest(
        @NotBlank String metricName,
        @NotBlank String serviceName,
        @NotNull Double threshold,
        @NotNull ThresholdDirection direction,
        @NotNull Long cooldownSeconds
) {
}
