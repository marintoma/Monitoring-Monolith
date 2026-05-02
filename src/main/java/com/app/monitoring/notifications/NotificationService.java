package com.app.monitoring.notifications;

import com.app.monitoring.notifications.dto.AlertRuleRequest;
import com.app.monitoring.notifications.dto.AlertRuleResponse;
import com.app.monitoring.notifications.dto.NotificationResponse;
import com.app.monitoring.notifications.entity.AlertRule;
import com.app.monitoring.notifications.entity.Notification;
import com.app.monitoring.notifications.events.MetricIngestedEvent;
import com.app.monitoring.notifications.repository.AlertRuleRepository;
import com.app.monitoring.notifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AlertRuleRepository alertRuleRepository;
    private final NotificationRepository notificationRepository;

    @Async
    @EventListener
    @Transactional
    public void onMetricIngested(MetricIngestedEvent event) {

        var metric = event.getMetric();

        alertRuleRepository.findByMetricNameAndServiceName(metric.getName(), metric.getServiceName())
                .forEach(alertRule -> {
                    if (isThresholdBreached(alertRule, metric.getValue()) && !isInCooldown(alertRule)) {
                        Notification notification = Notification.builder()
                                .alertRule(alertRule)
                                .triggerValue(metric.getValue())
                                .firedAt(Instant.now())
                                .build();
                        notificationRepository.save(notification);
                    }
                });
    }

    public AlertRuleResponse createAlertRule(AlertRuleRequest request) {
        AlertRule rule = AlertRule.builder()
                .metricName(request.metricName())
                .serviceName(request.serviceName())
                .threshold(request.threshold())
                .direction(request.direction())
                .cooldownSeconds(request.cooldownSeconds())
                .build();
        return toAlertRuleResponse(alertRuleRepository.save(rule));
    }

    @Transactional(readOnly = true)
    public Page<AlertRuleResponse> findAllAlertRules(Pageable pageable) {
        return alertRuleRepository.findAll(pageable)
                .map(this::toAlertRuleResponse);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> findAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable)
                .map(this::toNotificationResponse);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> findNotificationsByAlertRule(Long alertRuleId, Pageable pageable) {
        return notificationRepository.findByAlertRuleId(alertRuleId, pageable)
                .map(this::toNotificationResponse);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> findNotificationsByTimeRange(Instant from, Instant to, Pageable pageable) {
        return notificationRepository.findByFiredAtBetween(from, to, pageable)
                .map(this::toNotificationResponse);
    }

    private boolean isThresholdBreached(AlertRule rule, Double value) {
        return switch (rule.getDirection()) {
            case ABOVE -> value > rule.getThreshold();
            case BELOW -> value < rule.getThreshold();
        };
    }

    private boolean isInCooldown(AlertRule rule) {
        return notificationRepository.findTopByAlertRuleIdOrderByFiredAtDesc(rule.getId())
                .map(last -> last.getFiredAt()
                        .plusSeconds(rule.getCooldownSeconds())
                        .isAfter(Instant.now()))
                .orElse(false);
    }

    private AlertRuleResponse toAlertRuleResponse(AlertRule rule) {
        return AlertRuleResponse.builder()
                .id(rule.getId())
                .metricName(rule.getMetricName())
                .serviceName(rule.getServiceName())
                .threshold(rule.getThreshold())
                .direction(rule.getDirection())
                .cooldownSeconds(rule.getCooldownSeconds())
                .build();
    }

    private NotificationResponse toNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .alertRule(toAlertRuleResponse(notification.getAlertRule()))
                .triggerValue(notification.getTriggerValue())
                .firedAt(notification.getFiredAt())
                .build();
    }
}
