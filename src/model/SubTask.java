package model;

import java.time.LocalDateTime;

public class SubTask extends Task {

    private int idEpic;

    public SubTask(String title, String description, LocalDateTime startTime, int duration) {
        super(title, description, startTime, duration);
    }

    public SubTask(int id, String title, String description, Status status,
                   LocalDateTime startTime, int duration, int idEpic) {
        super(id, title, description, status, startTime, duration);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }
    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", idEpic=" + idEpic +
//  todo              ", title=" + getTitle() +
//                ", description=" + getDescription() +
                ", status=" + getStatus() +
                ", startTime=" + getStartTimeLine() +
                ", duration=" + getDurationHoursMinutesLine() +
                '}';
    }
}
