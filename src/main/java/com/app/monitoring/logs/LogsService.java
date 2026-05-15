package com.app.monitoring.logs;

import com.app.monitoring.common.BatchIngestResponse;
import com.app.monitoring.exceptions.BatchSizeExceededException;
import com.app.monitoring.logs.dto.LogRequest;
import com.app.monitoring.logs.dto.LogResponse;
import com.app.monitoring.logs.entity.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogsService {

    private final LogsRepository repo;

    @Value("${app.batch.max-size}")
    private int maxBatchSize;

    public LogResponse ingest(LogRequest request) {
        Log log = Log.builder()
                .level(request.level())
                .message(request.message())
                .serviceName(request.serviceName())
                .timestamp(request.timestamp())
                .build();
        return toResponse(repo.save(log));
    }

    @Transactional
    public BatchIngestResponse ingestBatch(List<LogRequest> requests) {
        if (requests.size() > maxBatchSize) {
            throw new BatchSizeExceededException(requests.size(), maxBatchSize);
        }

        int ingested = 0;
        int failed = 0;

        for (LogRequest request : requests) {
            try {
                Log log = Log.builder()
                        .level(request.level())
                        .message(request.message())
                        .serviceName(request.serviceName())
                        .timestamp(request.timestamp())
                        .build();
                repo.save(log);
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

    public Page<LogResponse> findByServiceName(String serviceName, Pageable pageable) {
        return repo.findByServiceName(serviceName, pageable)
                .map(this::toResponse);
    }

    public Page<LogResponse> findByLevel(LogLevel level, Pageable pageable) {
        return repo.findByLevel(level, pageable)
                .map(this::toResponse);
    }

    public Page<LogResponse> findByTimeRange(Instant from, Instant to, Pageable pageable) {
        return repo.findByTimestampBetween(from, to, pageable)
                .map(this::toResponse);
    }

    public Page<LogResponse> findByServiceNameAndTimeRange(String serviceName, Instant from, Instant to, Pageable pageable) {
        return repo.findByServiceNameAndTimestampBetween(serviceName, from, to, pageable)
                .map(this::toResponse);
    }

    public Page<LogResponse> findByLevelAndTimeRange(LogLevel level, Instant from, Instant to, Pageable pageable) {
        return repo.findByLevelAndTimestampBetween(level, from, to, pageable)
                .map(this::toResponse);
    }

    private LogResponse toResponse(Log log) {
        return LogResponse.builder()
                .id(log.getId())
                .level(log.getLevel())
                .message(log.getMessage())
                .serviceName(log.getServiceName())
                .timestamp(log.getTimestamp())
                .build();
    }
}
