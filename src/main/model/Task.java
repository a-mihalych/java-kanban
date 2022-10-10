package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private int id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime startTime;
    private int duration;

    public Task(String title, String description, LocalDateTime startTime, int duration) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
        if (duration > 0) {
            this.duration = duration;
        } else {
            this.duration = 0;
        }
    }

    public Task(int id, String title, String description, Status status, LocalDateTime startTime, int duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        if (duration > 0) {
            this.duration = duration;
        } else {
            this.duration = 0;
        }
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public String getStartTimeLine() {
        if (startTime != null) {
            return startTime.format(FORMATTER);
        }
        return "null";
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }
    public String getDurationHoursMinutesLine() {
        Duration duration = Duration.ofMinutes(this.duration);
        return String.format("%d:%02d", duration.toHours(), duration.toMinutesPart());
    }
    public void setDuration(int duration) {
        if (duration > 0) {
            this.duration = duration;
        } else {
            this.duration = 0;
        }
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(Duration.ofMinutes(duration));
        }
        return null;
    }
    public String getEndTimeLine() {
        if (getEndTime() != null) {
            return getEndTime().format(FORMATTER);
        }
        return "null";
    }

    public boolean isIntersectionTasks(Task task) {
        if ((startTime == null) || (duration == 0) || (task.getStartTime() == null) || (task.duration == 0)
            || startTime.isAfter(task.getEndTime()) || getEndTime().isBefore(task.getStartTime())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", status=" + status +
                ", startTime=" + getStartTimeLine() +
                ", duration=" + getDurationHoursMinutesLine() +
                '}';
    }
}
