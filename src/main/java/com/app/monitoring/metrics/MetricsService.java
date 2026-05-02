package com.app.monitoring.metrics;

import com.app.monitoring.metrics.dto.MetricRequest;
import com.app.monitoring.metrics.dto.MetricResponse;
import com.app.monitoring.metrics.entity.Metric;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final MetricsRepository repo;

    public MetricResponse ingest(MetricRequest metricRequest) {
        Metric metric = Metric.builder()
                .name(metricRequest.name())
                .value(metricRequest.value())
                .serviceName(metricRequest.serviceName())
                .timestamp(metricRequest.timestamp())
                .build();

        repo.save(metric);

        return toResponse(metric);
    }

    public Page<MetricResponse> findByServiceName(String serviceName, Pageable pageable) {
        return repo.findByServiceName(serviceName, pageable)
                .map(this::toResponse);
    }

    public Page<MetricResponse> findByName(String name, Pageable pageable) {
        return repo.findByName(name, pageable)
                .map(this::toResponse);
    }

    public Page<MetricResponse> findByTimestampBetween(Instant from, Instant to, Pageable pageable) {
        return repo.findByTimestampBetween(from, to, pageable)
                .map(this::toResponse);
    }

    public Page<MetricResponse> findByServiceNameAndTimeRange(String serviceName, Instant from, Instant to, Pageable pageable) {
        return repo.findByServiceNameAndTimestampBetween(serviceName, from, to, pageable)
                .map(this::toResponse);
    }

    private MetricResponse toResponse(Metric metric) {
        return MetricResponse.builder()
                .id(metric.getId())
                .name(metric.getName())
                .value(metric.getValue())
                .serviceName(metric.getServiceName())
                .timestamp(metric.getTimestamp())
                .build();
    }
}
