package com.app.monitoring.metrics;

import com.app.monitoring.common.PageResponse;
import com.app.monitoring.metrics.dto.MetricRequest;
import com.app.monitoring.metrics.dto.MetricResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MetricResponse ingest(@Valid @RequestBody MetricRequest metricRequest) {
        return service.ingest(metricRequest);
    }

    @GetMapping("/service/{serviceName}")
    public PageResponse<MetricResponse> getByServiceName(
            @PathVariable String serviceName,
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(service.findByServiceName(serviceName, pageable));
    }

    @GetMapping("/name/{name}")
    public PageResponse<MetricResponse> getByName(
            @PathVariable String name,
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(service.findByName(name, pageable));
    }

    @GetMapping("/range")
    public PageResponse<MetricResponse> getByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(service.findByTimestampBetween(from, to, pageable));
    }

    @GetMapping("/service/{serviceName}/range")
    public PageResponse<MetricResponse> getByServiceNameAndTimeRange(
            @PathVariable String serviceName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(service.findByServiceNameAndTimeRange(serviceName, from, to, pageable));
    }
}
