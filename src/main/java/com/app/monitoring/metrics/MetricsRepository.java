package com.app.monitoring.metrics;

import com.app.monitoring.metrics.entity.Metric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface MetricsRepository extends JpaRepository<Metric, Long> {

    Page<Metric> findByServiceName(String serviceName, Pageable pageable);

    Page<Metric> findByName(String metricName, Pageable pageable);

    Page<Metric> findByTimestampBetween(Instant from, Instant to, Pageable pageable);

    Page<Metric> findByServiceNameAndTimestampBetween(String serviceName, Instant from, Instant to, Pageable pageable);
}
