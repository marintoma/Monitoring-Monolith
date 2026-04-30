package com.app.monitoring.metrics;

import com.app.monitoring.metrics.entity.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MetricsRepository extends JpaRepository<Metric, Long> {

    List<Metric> findByServiceName(String serviceName);

    List<Metric> findByName(String metricName);

    List<Metric> findByTimestampBetween(Instant from, Instant to);

    List<Metric> findByServiceNameAndTimestampBetween(String serviceName, Instant from, Instant to);
}
