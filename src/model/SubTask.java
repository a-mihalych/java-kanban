package model;

public class SubTask extends Task {

    private int idEpic;

    public SubTask(String title, String description) {
        super(title, description);
    }

    public SubTask(int id, String title, String description, Status status, int idEpic) {
        super(id, title, description, status);
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
                ", title='" + getTitle() + '\'' +
                ", description=" + getDescription() +
                ", status=" + getStatus() +
                '}';
    }
}
