package com.app.monitoring.logs;

import com.app.monitoring.logs.dto.LogRequest;
import com.app.monitoring.logs.dto.LogResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

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
    public List<LogResponse> getByServiceName(@PathVariable String serviceName) {
        return service.findByServiceName(serviceName);
    }

    @GetMapping("/level/{level}")
    public List<LogResponse> getByLevel(@PathVariable LogLevel level) {
        return service.findByLevel(level);
    }

    @GetMapping("/range")
    public List<LogResponse> getByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return service.findByTimeRange(from, to);
    }

    @GetMapping("/service/{serviceName}/range")
    public List<LogResponse> getByServiceNameAndTimeRange(
            @PathVariable String serviceName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return service.findByServiceNameAndTimeRange(serviceName, from, to);
    }

    @GetMapping("/level/{level}/range")
    public List<LogResponse> getByLevelAndTimeRange(
            @PathVariable LogLevel level,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return service.findByLevelAndTimeRange(level, from, to);
    }
}
