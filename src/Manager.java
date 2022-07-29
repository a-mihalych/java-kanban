import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Manager {

    private int id = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    // (2.1)Получение списка всех задач/подзадач/эпиков
    public Collection<Task> getTasks() {
        return tasks.values();
    }

    public Collection<SubTask> getSubTasks() {
        return subTasks.values();
    }

    public Collection<Epic> getEpics() {
        return epics.values();
    }

    // (2.2)Удаление всех задач/подзадач/эпиков
    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubTasks() {
        subTasks.clear();
        // пустые epics - статус NEW; очиска у epic списка id подзадач
        for (int idEpic : epics.keySet()) {
            epics.get(idEpic).setStatus(Status.NEW);
            epics.get(idEpic).getIdSubTasks().clear();
        }
    }

    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    // (2.3)Получение по идентификатору задач/подзадач/эпиков
    public Task getTask(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    // (2.4)Создание. Сам объект должен передаваться в качестве параметра.
    public void creationTask(Task task) {
        task.setId(id++);
        tasks.put(task.getId(), task);
    }

    public int creationSubTask(SubTask subTask) {
        subTask.setId(id++);
        subTasks.put(subTask.getId(), subTask);
        return subTask.getId();
    }

    public int creationEpic(Epic epic) {
        epic.setId(id++);
        epics.put(epic.getId(), epic);
        changeStatus(epic.getId());
        return epic.getId();
    }

    // (2.5)Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        changeStatus(subTask.getIdEpic());
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        changeStatus(epic.getId());
    }

    // (2.6)Удаление по идентификатору.
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteSubTask(int id) {
        int idEpic = subTasks.get(id).getIdEpic();
        epics.get(idEpic).getIdSubTasks().remove(id);
        subTasks.remove(id);
        changeStatus(idEpic);
    }

    public void deleteEpic(int id) {
        // удаление подзадач удаляемого epic
        for (SubTask subTask : getSubTasksEpic(id)) {
            subTasks.remove(subTask.getId());
        }
        epics.remove(id);
    }

    // (3.1)Получение списка всех подзадач определённого эпика
    private ArrayList<SubTask> getSubTasksEpic(int id) {
        ArrayList<SubTask> subTasksEpic = new ArrayList<>();
        for (int idSubTask : epics.get(id).getIdSubTasks()) {
            subTasksEpic.add(subTasks.get(idSubTask));
        }
        return subTasksEpic;
    }

    // (4.2)проверка и изменение статуса эпика
    private void changeStatus(int id) {
        Status status;
        ArrayList<SubTask> SubTasksEpic = getSubTasksEpic(id);
        if (SubTasksEpic.size() > 1) {
            Status statusOld;
            status = SubTasksEpic.get(0).getStatus();
            int i = 1;
            do {
                statusOld = status;
                status = SubTasksEpic.get(i).getStatus();
                i++;
            } while ((SubTasksEpic.size() > i) && (status != Status.IN_PROGRESS) && (status == statusOld));
            if (status != statusOld) {
                status = Status.IN_PROGRESS;
            }
        } else {
            if (SubTasksEpic.isEmpty()) {
                status = Status.NEW;
            } else {
                status = SubTasksEpic.get(0).getStatus();
            }
        }
        epics.get(id).setStatus(status);
    }
}
