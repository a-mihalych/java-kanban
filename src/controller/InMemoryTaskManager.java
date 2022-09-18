package controller;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int id = 1;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    public void restoreId() {
        int maxIdTask = 0;
        int maxIdSubTask = 0;
        int maxIdEpic = 0;
        if (!tasks.isEmpty()) {
            maxIdTask = Collections.max(tasks.keySet());
        }
        if (!subTasks.isEmpty()) {
            maxIdSubTask = Collections.max(subTasks.keySet());
        }
        if (!epics.isEmpty()) {
            maxIdEpic = Collections.max(epics.keySet());
        }
        id = Math.max(maxIdTask, Math.max(maxIdSubTask, maxIdEpic)) + 1;
    }

    public void setTasks(Map<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    public void setSubTasks(Map<Integer, SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public void setEpics(Map<Integer, Epic> epics) {
        this.epics = epics;
    }

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

    @Override
    public void deleteTasks() {
        deleteHistory(tasks.keySet());
        tasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        deleteHistory(subTasks.keySet());
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            epic.getIdSubTasks().clear();
        }
    }

    @Override
    public void deleteEpics() {
        deleteHistory(epics.keySet());
        deleteHistory(subTasks.keySet());
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Task getTask(int id) {
        Managers.getDefaultHistory().add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        Managers.getDefaultHistory().add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        Managers.getDefaultHistory().add(epics.get(id));
        return epics.get(id);
    }

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

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        Managers.getDefaultHistory().remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        int idEpic = subTasks.get(id).getIdEpic();
        epics.get(idEpic).getIdSubTasks().remove(id);
        subTasks.remove(id);
        changeStatus(idEpic);
        Managers.getDefaultHistory().remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        for (SubTask subTask : getSubTasksByEpicId(id)) {
            subTasks.remove(subTask.getId());
            Managers.getDefaultHistory().remove(subTask.getId());
        }
        epics.remove(id);
        Managers.getDefaultHistory().remove(id);
    }

    private List<SubTask> getSubTasksByEpicId(int id) {
        List<SubTask> subTasksEpic = new ArrayList<>();
        for (int idSubTask : epics.get(id).getIdSubTasks()) {
            subTasksEpic.add(subTasks.get(idSubTask));
        }
        return subTasksEpic;
    }

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

    private void deleteHistory(Set<Integer> keys) {
        for (int key : keys) {
            Managers.getDefaultHistory().remove(key);
        }
    }
}
