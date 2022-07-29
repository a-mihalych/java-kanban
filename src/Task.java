import java.util.ArrayList;

public class Task {

    private int id;
    private String title;
    private ArrayList<String> description;
    private Status status;

    public Task(String title, ArrayList<String> description) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
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

    public ArrayList<String> getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
