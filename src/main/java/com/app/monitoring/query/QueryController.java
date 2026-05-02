package com.app.monitoring.query;

import com.app.monitoring.common.PageResponse;
import com.app.monitoring.logs.LogsService;
import com.app.monitoring.metrics.MetricsService;
import com.app.monitoring.query.dto.QueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("api/query")
@RequiredArgsConstructor
public class QueryController {

    private final MetricsService metricsService;
    private final LogsService logsService;

    @GetMapping
    public QueryResponse query(
            @RequestParam String serviceName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(defaultValue = "0") int metricsPage,
            @RequestParam(defaultValue = "50") int metricsSize,
            @RequestParam(defaultValue = "0") int logsPage,
            @RequestParam(defaultValue = "50") int logsSize
    ) {
        return QueryResponse.builder()
                .serviceName(serviceName)
                .from(from)
                .to(to)
                .metrics(PageResponse.from(metricsService
                        .findByServiceNameAndTimeRange(serviceName, from, to, PageRequest.of(metricsPage, metricsSize))))
                .logs(PageResponse.from(logsService
                        .findByServiceNameAndTimeRange(serviceName, from, to, PageRequest.of(logsPage, logsSize))))
                .build();
    }
}
