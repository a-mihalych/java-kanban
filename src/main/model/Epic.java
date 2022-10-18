package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> idSubTasks;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, null, 0);
        this.idSubTasks = new ArrayList<>();
    }

    public Epic(int id, String title, String description, Status status,
                LocalDateTime startTime, int duration, ArrayList<Integer> idSubTasks) {
        super(id, title, description, status, startTime, duration);
        this.idSubTasks = idSubTasks;
    }

    public Epic(Epic epic) {
        super(epic.getId(), epic.getTitle(), epic.getDescription(),
              epic.getStatus(), epic.getStartTime(), epic.getDuration());
        if (epic.getStatus() == null) {
            epic.setStatus(Status.NEW);
        }
        if (this.idSubTasks == null) {
            this.idSubTasks = new ArrayList<>();
        }
    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void addIdSubTask(int idSubTask) {
        idSubTasks.add(idSubTask);
    }

    public void setDuration() {
        if ((getStartTime() != null) && (endTime != null)) {
            setDuration((int) Duration.between(getStartTime(), endTime).toMinutes());
        } else {
            setDuration(0);
        }
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
                ", title=" + getTitle() +
                ", description=" + getDescription() +
                ", status=" + getStatus() +
                ", startTime=" + getStartTimeLine() +
                ", duration=" + getDurationHoursMinutesLine() +
                ", endTime=" + getEndTimeLine() +
                '}';

    }
}
