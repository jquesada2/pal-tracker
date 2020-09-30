package io.pivotal.pal.tracker;

import java.time.LocalDate;
import java.util.Objects;

public class TimeEntry {

    private long id;
    private long projectId;
    private long userId;
    private LocalDate date;
    private int hours;

    public long getId() {
        return id;
    }

    public TimeEntry setId(long id) {
        this.id = id;
        return this;
    }

    public long getProjectId() {
        return projectId;
    }

    public TimeEntry setProjectId(long projectId) {
        this.projectId = projectId;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public TimeEntry setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public TimeEntry setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public int getHours() {
        return hours;
    }

    public TimeEntry setHours(int hours) {
        this.hours = hours;
        return this;
    }

    public TimeEntry() {
    }

    public TimeEntry(long projectId, long userId, LocalDate date, int hours) {
        setProjectId(projectId).setUserId(userId).setDate(date).setHours(hours);
    }

    public TimeEntry(long id, long projectId, long userId, LocalDate date, int hours) {
        this(projectId, userId, date, hours);
        setId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeEntry)) return false;
        TimeEntry timeEntry = (TimeEntry) o;
        return getId() == timeEntry.getId() &&
                getProjectId() == timeEntry.getProjectId() &&
                getUserId() == timeEntry.getUserId() &&
                getHours() == timeEntry.getHours() &&
                getDate().isEqual(timeEntry.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProjectId(), getUserId(), getDate(), getHours());
    }

    @Override
    public String toString() {
        return "TimeEntry{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", userId=" + userId +
                ", date=" + date +
                ", hours=" + hours +
                '}';
    }
}
