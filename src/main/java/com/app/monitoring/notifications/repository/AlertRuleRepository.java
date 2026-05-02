package com.app.monitoring.notifications.repository;

import com.app.monitoring.notifications.entity.AlertRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRuleRepository extends JpaRepository<AlertRule, Long> {

    List<AlertRule> findByMetricNameAndServiceName(String metricName, String serviceName);
}
