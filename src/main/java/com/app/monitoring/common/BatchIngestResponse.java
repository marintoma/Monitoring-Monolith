package com.app.monitoring.common;

import lombok.Builder;

import java.time.Instant;

@Builder
public record BatchIngestResponse(
        int ingested,
        int failed,
        Instant timestamp
) {
}
