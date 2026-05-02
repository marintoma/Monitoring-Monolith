package com.app.monitoring.notifications;

import com.app.monitoring.common.PageResponse;
import com.app.monitoring.notifications.dto.AlertRuleRequest;
import com.app.monitoring.notifications.dto.AlertRuleResponse;
import com.app.monitoring.notifications.dto.NotificationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/rules")
    @ResponseStatus(HttpStatus.CREATED)
    public AlertRuleResponse createAlertRule(@Valid @RequestBody AlertRuleRequest request) {
        return notificationService.createAlertRule(request);
    }

    @GetMapping("/rules")
    public PageResponse<AlertRuleResponse> getAllAlertRules(
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(notificationService.findAllAlertRules(pageable));
    }

    @GetMapping
    public PageResponse<NotificationResponse> getAllNotifications(
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(notificationService.findAllNotifications(pageable));
    }

    @GetMapping("/rule/{alertRuleId}")
    public PageResponse<NotificationResponse> getNotificationsByAlertRule(
            @PathVariable Long alertRuleId,
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(notificationService.findNotificationsByAlertRule(alertRuleId, pageable));
    }

    @GetMapping("/range")
    public PageResponse<NotificationResponse> getNotificationsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @PageableDefault(size = 50) Pageable pageable) {
        return PageResponse.from(notificationService.findNotificationsByTimeRange(from, to, pageable));
    }
}
