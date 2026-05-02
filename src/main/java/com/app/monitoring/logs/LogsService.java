package com.app.monitoring.logs;

import com.app.monitoring.logs.dto.LogRequest;
import com.app.monitoring.logs.dto.LogResponse;
import com.app.monitoring.logs.entity.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LogsService {

    private final LogsRepository repo;

    public LogResponse ingest(LogRequest request) {
        Log log = Log.builder()
                .level(request.level())
                .message(request.message())
                .serviceName(request.serviceName())
                .timestamp(request.timestamp())
                .build();
        return toResponse(repo.save(log));
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
