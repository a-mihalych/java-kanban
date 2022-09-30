package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {

    private int id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime startTime;
    private Duration duration;

    public Task(String title, String description, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
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
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description=" + description +
                ", status=" + status +
                '}';
    }
}
