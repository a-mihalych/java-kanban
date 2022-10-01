package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> idSubTasks;
    private LocalDateTime endTime;

    public Epic(String title, String description, LocalDateTime startTime, Duration duration) {
        super(title, description, startTime, duration);
        this.idSubTasks = new ArrayList<>();
        this.endTime = startTime.plus(duration);
    }

    public Epic(int id, String title, String description, Status status,
                LocalDateTime startTime, Duration duration, ArrayList<Integer> idSubTasks) {
        super(id, title, description, status, startTime, duration);
        this.idSubTasks = idSubTasks;
        this.endTime = startTime.plus(duration);
    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void addIdSubTask(int idSubTask) {
        idSubTasks.add(idSubTask);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", idSubTasks=" + idSubTasks +
                ", title='" + getTitle() + '\'' +
                ", description=" + getDescription() +
                ", status=" + getStatus() +
                ", startTime=" + getStartTimeLine() +
                ", duration=" + getDurationLine() +
                ", endTime=" + getEndTimeLine() +
                '}';

    }
}
