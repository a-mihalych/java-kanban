package controller;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.Collection;

public interface TaskManager {

    // (2.1)Получение списка всех задач/подзадач/эпиков
    Collection<Task> getTasks();
    Collection<SubTask> getSubTasks();
    Collection<Epic> getEpics();

    // (2.2)Удаление всех задач/подзадач/эпиков
    void deleteTasks();
    void deleteSubTasks();
    void deleteEpics();

    // (2.3)Получение по идентификатору задач/подзадач/эпиков
    Task getTask(int id);
    SubTask getSubTask(int id);
    Epic getEpic(int id);

    // (2.4)Создание. Сам объект должен передаваться в качестве параметра.
    void createTask(Task task);
    void createSubTask(SubTask subTask, int idEpic);
    int createEpic(Epic epic);

    // (2.5)Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateTask(Task task);
    void updateSubTask(SubTask subTask);
    void updateEpic(Epic epic);

    // (2.6)Удаление по идентификатору.
    void deleteTask(int id);
    void deleteSubTask(int id);
    void deleteEpic(int id);
}
