package com.app.monitoring.metrics;

import com.app.monitoring.common.BatchIngestResponse;
import com.app.monitoring.exceptions.BatchSizeExceededException;
import com.app.monitoring.metrics.dto.MetricRequest;
import com.app.monitoring.metrics.dto.MetricResponse;
import com.app.monitoring.metrics.entity.Metric;
import com.app.monitoring.notifications.events.MetricIngestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final MetricsRepository repo;
    private final ApplicationEventPublisher publisher;

    @Value("${app.batch.max-size}")
    private int maxBatchSize;

    public MetricResponse ingest(MetricRequest metricRequest) {
        Metric metric = Metric.builder()
                .name(metricRequest.name())
                .value(metricRequest.value())
                .serviceName(metricRequest.serviceName())
                .timestamp(metricRequest.timestamp())
                .build();

        Metric saved = repo.save(metric);
        publisher.publishEvent(new MetricIngestedEvent(this, saved));

        return toResponse(metric);
    }

    @Transactional
    public BatchIngestResponse ingestBatch(List<MetricRequest> requests) {
        if (requests.size() > maxBatchSize) {
            throw new BatchSizeExceededException(requests.size(), maxBatchSize);
        }

        int ingested = 0;
        int failed = 0;

        for (MetricRequest request : requests) {
            try {
                Metric metric = Metric.builder()
                        .name(request.name())
                        .value(request.value())
                        .serviceName(request.serviceName())
                        .timestamp(request.timestamp())
                        .build();
                Metric saved = repo.save(metric);
                publisher.publishEvent(new MetricIngestedEvent(this, saved));
                ingested++;
            } catch (Exception e) {
                failed++;
            }
        }

        return BatchIngestResponse.builder()
                .ingested(ingested)
                .failed(failed)
                .timestamp(Instant.now())
                .build();
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
