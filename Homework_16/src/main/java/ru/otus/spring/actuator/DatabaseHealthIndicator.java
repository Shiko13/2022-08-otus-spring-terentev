package ru.otus.spring.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Health health() {
        try {
            jdbcTemplate.execute("select 1");
            return Health.up()
                    .withDetail("message", "Database is up")
                    .build();
        } catch (Throwable t) {
            return Health.down()
                    .withDetail("message", "Database is down")
                    .build();
        }
    }
}
