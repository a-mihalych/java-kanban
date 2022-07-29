import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> idSubTasks;

    public Epic(String title, ArrayList<String> description) {
        super(title, description);
        this.idSubTasks = new ArrayList<>();
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
