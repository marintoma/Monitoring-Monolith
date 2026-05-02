CREATE TABLE alert_rules (
    id               BIGSERIAL PRIMARY KEY,
    metric_name      VARCHAR(255) NOT NULL,
    service_name     VARCHAR(100) NOT NULL,
    threshold        DOUBLE PRECISION NOT NULL,
    direction        VARCHAR(10) NOT NULL,
    cooldown_seconds BIGINT NOT NULL
);

CREATE TABLE notifications (
    id            BIGSERIAL PRIMARY KEY,
    alert_rule_id BIGINT NOT NULL REFERENCES alert_rules(id),
    trigger_value DOUBLE PRECISION NOT NULL,
    fired_at      TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_notifications_alert_rule_id ON notifications(alert_rule_id);
CREATE INDEX idx_notifications_fired_at ON notifications(fired_at);
CREATE INDEX idx_alert_rules_metric_name ON alert_rules(metric_name);
CREATE INDEX idx_alert_rules_service_name ON alert_rules(service_name);