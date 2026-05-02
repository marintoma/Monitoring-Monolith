package com.app.monitoring.logs;

import com.app.monitoring.logs.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface LogsRepository extends JpaRepository<Log, Long> {

    List<Log> findByServiceName(String serviceName);

    List<Log> findByLevel(LogLevel level);

    List<Log> findByTimestampBetween(Instant from, Instant to);

    List<Log> findByServiceNameAndTimestampBetween(String serviceName, Instant from, Instant to);

    List<Log> findByLevelAndTimestampBetween(LogLevel level, Instant from, Instant to);
}
