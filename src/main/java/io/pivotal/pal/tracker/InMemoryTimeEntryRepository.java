package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    final AtomicLong nextId = new AtomicLong(1);
    private Map<Long, TimeEntry> timeEntryData = new HashMap<>();

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        var newTimeEntry = new TimeEntry()
                .setId(nextId.getAndIncrement())
                .setProjectId(timeEntry.getProjectId())
                .setUserId(timeEntry.getUserId())
                .setDate(timeEntry.getDate())
                .setHours(timeEntry.getHours());

        timeEntryData.put(newTimeEntry.getId(), newTimeEntry);

        return newTimeEntry;
    }

    @Override
    public TimeEntry find(Long id) {
        return timeEntryData.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntryData.values());
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        var entry = find(id);
        if (entry != null) {
            entry
                    .setHours(timeEntry.getHours())
                    .setDate(timeEntry.getDate())
                    .setProjectId(timeEntry.getProjectId())
                    .setUserId(timeEntry.getUserId());
        }

        return entry;
    }

    @Override
    public void delete(Long id) {
        timeEntryData.remove(id);
    }
}
