package model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> idSubTasks;

    public Epic(String title, String description) {
        super(title, description);
        this.idSubTasks = new ArrayList<>();
    }

    public Epic(int id, String title, String description, Status status, ArrayList<Integer> idSubTasks) {
        super(id, title, description, status);
        this.idSubTasks = idSubTasks;
    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void addIdSubTask(int idSubTask) {
        idSubTasks.add(idSubTask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", idSubTasks=" + idSubTasks +
                ", title='" + getTitle() + '\'' +
                ", description=" + getDescription() +
                ", status=" + getStatus() +
                '}';
    }
}
