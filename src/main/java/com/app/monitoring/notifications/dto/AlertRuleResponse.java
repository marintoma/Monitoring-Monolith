package com.app.monitoring.notifications.dto;

import com.app.monitoring.notifications.ThresholdDirection;
import lombok.Builder;

@Builder
public record AlertRuleResponse(
        Long id,
        String metricName,
        String serviceName,
        Double threshold,
        ThresholdDirection direction,
        Long cooldownSeconds
) {
}
