package com.app.monitoring.logs;

import com.app.monitoring.logs.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface LogsRepository extends JpaRepository<Log, Long> {

    Page<Log> findByServiceName(String serviceName, Pageable pageable);

    Page<Log> findByLevel(LogLevel level, Pageable pageable);

    Page<Log> findByTimestampBetween(Instant from, Instant to, Pageable pageable);

    Page<Log> findByServiceNameAndTimestampBetween(String serviceName, Instant from, Instant to, Pageable pageable);

    Page<Log> findByLevelAndTimestampBetween(LogLevel level, Instant from, Instant to, Pageable pageable);
}
