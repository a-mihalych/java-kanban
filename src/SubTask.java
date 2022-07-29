import java.util.ArrayList;

public class SubTask extends Task {

    private int idEpic;

    public SubTask(String title, ArrayList<String> description) {
        super(title, description);
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
