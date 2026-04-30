package com.app.monitoring.metrics;

import com.app.monitoring.metrics.dto.MetricRequest;
import com.app.monitoring.metrics.dto.MetricResponse;
import com.app.monitoring.metrics.entity.Metric;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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

    public List<MetricResponse> findByServiceName(String serviceName) {
        return repo.findByServiceName(serviceName)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MetricResponse> findByName(String name) {
        return repo.findByName(name)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MetricResponse> findByTimestampBetween(Instant from, Instant to) {
        return repo.findByTimestampBetween(from, to)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MetricResponse> findByServiceNameAndTimeRange(String serviceName, Instant from, Instant to) {
        return repo.findByServiceNameAndTimestampBetween(serviceName, from, to)
                .stream()
                .map(this::toResponse)
                .toList();
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
