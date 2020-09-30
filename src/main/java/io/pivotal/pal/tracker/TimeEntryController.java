package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        var created = timeEntryRepository.create(timeEntryToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        var entry = timeEntryRepository.find(id);
        if (entry == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(entry);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        var entries = timeEntryRepository.list();

        return ResponseEntity.ok(entries);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable  long id, @RequestBody TimeEntry expected) {
        var entry = timeEntryRepository.update(id, expected);
        if (entry == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(entry);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id) {
        timeEntryRepository.delete(id);

        return ResponseEntity.noContent().build();
    }
}
