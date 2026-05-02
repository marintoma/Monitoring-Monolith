CREATE TABLE metrics (
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    value     DOUBLE PRECISION NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_metrics_service_name ON metrics(service_name);
CREATE INDEX idx_metrics_name ON metrics(name);
CREATE INDEX idx_metrics_timestamp ON metrics(timestamp);
CREATE INDEX idx_metrics_service_name_timestamp ON metrics(service_name, timestamp);