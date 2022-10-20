package controller.interfaces;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.Collection;

public interface TaskManager {

    Collection<Task> getTasks();
    Collection<SubTask> getSubTasks();
    Collection<Epic> getEpics();

    void deleteTasks();
    void deleteSubTasks();
    void deleteEpics();

    Task getTask(int id);
    SubTask getSubTask(int id);
    Epic getEpic(int id);

    void createTask(Task task);
    void createSubTask(SubTask subTask, int idEpic);
    int createEpic(Epic epic);

    void updateTask(Task task);
    void updateSubTask(SubTask subTask);
    void updateEpic(Epic epic);

    void deleteTask(int id);
    void deleteSubTask(int id);
    void deleteEpic(int id);
}
