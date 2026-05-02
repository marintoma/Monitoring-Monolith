package com.app.monitoring.notifications.repository;

import com.app.monitoring.notifications.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByAlertRuleId(Long alertRuleid, Pageable pageable);

    Optional<Notification> findTopByAlertRuleIdOrderByFiredAtDesc(Long alertRuleid);

    Page<Notification> findByFiredAtBetween(Instant from, Instant to, Pageable pageable);
}
