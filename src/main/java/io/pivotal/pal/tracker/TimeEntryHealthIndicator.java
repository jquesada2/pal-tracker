package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {
    final int TIME_ENTRY_COUNT_THRESHOLD = 5;
    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryHealthIndicator(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @Override
    public Health health() {
        var count = timeEntryRepository.list().size();

        if (count < TIME_ENTRY_COUNT_THRESHOLD) {
            return Health.up().build();
        } else {
            return Health.down().build();
        }
    }
}
