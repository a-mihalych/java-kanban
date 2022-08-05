package controller;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final int MAX_VIEVS = 10;
    private int id = 1;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private List<Task> views = new ArrayList<>();

    // (2.1)Получение списка всех задач/подзадач/эпиков
    @Override
    public Collection<Task> getTasks() {
        return tasks.values();
    }

    @Override
    public Collection<SubTask> getSubTasks() {
        return subTasks.values();
    }

    @Override
    public Collection<Epic> getEpics() {
        return epics.values();
    }

    // (2.2)Удаление всех задач/подзадач/эпиков
    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        subTasks.clear();
        // пустые epics - статус NEW; очиска у epic списка id подзадач
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            epic.getIdSubTasks().clear();
        }
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    // (2.3)Получение по идентификатору задач/подзадач/эпиков
    @Override
    public Task getTask(int id) {
        addView(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        addView(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        addView(epics.get(id));
        return epics.get(id);
    }

    // (2.4)Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public void createTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createSubTask(SubTask subTask, int idEpic) {
        subTask.setId(getNextId());
        subTask.setIdEpic(idEpic);
        epics.get(idEpic).addIdSubTask(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
        changeStatus(idEpic);
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        changeStatus(epic.getId());
        return epic.getId();
    }

    // (2.5)Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        changeStatus(subTask.getIdEpic());
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        changeStatus(epic.getId());
    }

    // (2.6)Удаление по идентификатору.
    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        int idEpic = subTasks.get(id).getIdEpic();
        epics.get(idEpic).getIdSubTasks().remove(id);
        subTasks.remove(id);
        changeStatus(idEpic);
    }

    @Override
    public void deleteEpic(int id) {
        // удаление подзадач удаляемого epic
        for (SubTask subTask : getSubTasksByEpicId(id)) {
            subTasks.remove(subTask.getId());
        }
        epics.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return views;
    }

    // (3.1)Получение списка всех подзадач определённого эпика
    private List<SubTask> getSubTasksByEpicId(int id) {
        List<SubTask> subTasksEpic = new ArrayList<>();
        for (int idSubTask : epics.get(id).getIdSubTasks()) {
            subTasksEpic.add(subTasks.get(idSubTask));
        }
        return subTasksEpic;
    }

    // (4.2)проверка и изменение статуса эпика
    private void changeStatus(int id) {
        Status status;
        List<SubTask> subTasksEpic = getSubTasksByEpicId(id);
        if (subTasksEpic.size() > 1) {
            Status statusOld;
            status = subTasksEpic.get(0).getStatus();
            int i = 1;
            do {
                statusOld = status;
                status = subTasksEpic.get(i).getStatus();
                i++;
            } while ((subTasksEpic.size() > i) && (status != Status.IN_PROGRESS) && (status == statusOld));
            if (status != statusOld) {
                status = Status.IN_PROGRESS;
            }
        } else {
            if (subTasksEpic.isEmpty()) {
                status = Status.NEW;
            } else {
                status = subTasksEpic.get(0).getStatus();
            }
        }
        epics.get(id).setStatus(status);
    }

    private int getNextId() {
        return id++;
    }

    private void addView(Task task) {
        if (views.size() == MAX_VIEVS) {
            views.remove(0);
        }
        views.add(task);
    }
}
