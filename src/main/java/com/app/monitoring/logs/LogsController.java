package com.app.monitoring.logs;

import com.app.monitoring.common.PageResponse;
import com.app.monitoring.logs.dto.LogRequest;
import com.app.monitoring.logs.dto.LogResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogsController {

    private final LogsService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogResponse ingest(@Valid @RequestBody LogRequest request) {
        return service.ingest(request);
    }

    @GetMapping("/service/{serviceName}")
    public PageResponse<LogResponse> getByServiceName(
            @PathVariable String serviceName,
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(service.findByServiceName(serviceName, pageable));
    }

    @GetMapping("/level/{level}")
    public PageResponse<LogResponse> getByLevel(
            @PathVariable LogLevel level,
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(service.findByLevel(level, pageable));
    }

    @GetMapping("/range")
    public PageResponse<LogResponse> getByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(service.findByTimeRange(from, to, pageable));
    }

    @GetMapping("/service/{serviceName}/range")
    public PageResponse<LogResponse> getByServiceNameAndTimeRange(
            @PathVariable String serviceName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(service.findByServiceNameAndTimeRange(serviceName, from, to, pageable));
    }

    @GetMapping("/level/{level}/range")
    public PageResponse<LogResponse> getByLevelAndTimeRange(
            @PathVariable LogLevel level,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(service.findByLevelAndTimeRange(level, from, to, pageable));
    }
}
