package com.app.monitoring.logs;

import com.app.monitoring.logs.dto.LogRequest;
import com.app.monitoring.logs.dto.LogResponse;
import com.app.monitoring.logs.entity.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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

    public List<LogResponse> findByServiceName(String serviceName) {
        return repo.findByServiceName(serviceName)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<LogResponse> findByLevel(LogLevel level) {
        return repo.findByLevel(level)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<LogResponse> findByTimeRange(Instant from, Instant to) {
        return repo.findByTimestampBetween(from, to)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<LogResponse> findByServiceNameAndTimeRange(String serviceName, Instant from, Instant to) {
        return repo.findByServiceNameAndTimestampBetween(serviceName, from, to)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<LogResponse> findByLevelAndTimeRange(LogLevel level, Instant from, Instant to) {
        return repo.findByLevelAndTimestampBetween(level, from, to)
                .stream()
                .map(this::toResponse)
                .toList();
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
