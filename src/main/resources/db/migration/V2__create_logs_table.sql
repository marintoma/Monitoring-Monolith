CREATE TABLE logs (
    id              BIGSERIAL PRIMARY KEY,
    level           VARCHAR(10) NOT NULL,
    message         TEXT NOT NULL,
    service_name    VARCHAR(100) NOT NULL,
    timestamp       TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_logs_service_name ON logs(service_name);
CREATE INDEX idx_logs_level ON logs(level);
CREATE INDEX idx_logs_timestamp ON logs(timestamp);
CREATE INDEX idx_logs_service_name_timestamp ON logs(service_name, timestamp);
CREATE INDEX idx_logs_level_timestamp ON logs(level, timestamp);