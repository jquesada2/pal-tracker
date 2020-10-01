package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        this.timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        this.actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        var created = timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        var entry = timeEntryRepository.find(id);
        if (entry == null) {
            return ResponseEntity.notFound().build();
        }

        actionCounter.increment();
        return ResponseEntity.ok(entry);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        var entries = timeEntryRepository.list();
        actionCounter.increment();

        return ResponseEntity.ok(entries);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable  long id, @RequestBody TimeEntry expected) {
        var entry = timeEntryRepository.update(id, expected);
        if (entry == null) {
            return ResponseEntity.notFound().build();
        }

        actionCounter.increment();
        return ResponseEntity.ok(entry);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id) {
        timeEntryRepository.delete(id);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        return ResponseEntity.noContent().build();
    }
}
