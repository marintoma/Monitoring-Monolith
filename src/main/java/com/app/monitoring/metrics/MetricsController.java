package com.app.monitoring.metrics;

import com.app.monitoring.metrics.dto.MetricRequest;
import com.app.monitoring.metrics.dto.MetricResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

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
    public List<MetricResponse> getByServiceName(@PathVariable String serviceName) {
        return service.findByServiceName(serviceName);
    }

    @GetMapping("/name/{name}")
    public List<MetricResponse> getByName(@PathVariable String name) {
        return service.findByName(name);
    }

    @GetMapping("/range")
    public List<MetricResponse> getByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return service.findByTimestampBetween(from, to);
    }

    @GetMapping("/service/{serviceName}/range")
    public List<MetricResponse> getByServiceNameAndTimeRange(
            @PathVariable String serviceName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return service.findByServiceNameAndTimeRange(serviceName, from, to);
    }
}
