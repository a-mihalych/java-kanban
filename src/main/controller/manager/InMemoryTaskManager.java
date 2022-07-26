package controller.manager;

import controller.interfaces.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int id = 1;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private final Set<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> {
        if ((task1.getStartTime() != null) || (task2.getStartTime() != null)) {
            if (task1.getStartTime() == null) {
                return 1;
            }
            if ((task2.getStartTime() == null) || (task1.getStartTime().isBefore(task2.getStartTime()))) {
                return -1;
            }
            if (task1.getStartTime().isAfter(task2.getStartTime())) {
                return 1;
            }
        }
        return task1.getId() - task2.getId();
    });

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

    public void addPrioritizedTasks() {
        if (!tasks.isEmpty()) {
            prioritizedTasks.addAll(tasks.values());
        }
        if (!subTasks.isEmpty()) {
            prioritizedTasks.addAll(subTasks.values());
        }
    }

    public List<Task> getPrioritizedTasks () {
        return new ArrayList<>(prioritizedTasks);
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
        for (Task task : tasks.values()) {
            prioritizedTasks.remove(task);
        }
        deleteHistory(tasks.keySet());
        tasks.clear();
    }

    private void removeSubTasksFromPriorityList() {
        for (Task subTask : subTasks.values()) {
            prioritizedTasks.remove(subTask);
        }
    }

    @Override
    public void deleteSubTasks() {
        removeSubTasksFromPriorityList();
        deleteHistory(subTasks.keySet());
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            epic.getIdSubTasks().clear();
        }
    }

    @Override
    public void deleteEpics() {
        removeSubTasksFromPriorityList();
        deleteHistory(epics.keySet());
        deleteHistory(subTasks.keySet());
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            Managers.getDefaultHistory().add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public SubTask getSubTask(int id) {
        if (subTasks.containsKey(id)) {
            Managers.getDefaultHistory().add(subTasks.get(id));
            return subTasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpic(int id) {
        if (epics.containsKey(id)) {
            Managers.getDefaultHistory().add(epics.get(id));
            return epics.get(id);
        }
        return null;
    }

    @Override
    public void createTask(Task task) {
        if (isIntersectionTasks(getPrioritizedTasks(), task)) {
            System.out.printf("Задача, %s, не может быть создана, на это время уже назначена другая задача.\n",
                              task.getTitle());
        } else {
            task.setId(getNextId());
            if (task.getStatus() == null) {
                task.setStatus(Status.NEW);
            }
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void createSubTask(SubTask subTask, int idEpic) {
        if (isIntersectionTasks(getPrioritizedTasks(), subTask)) {
            System.out.printf("Задача, %s, не может быть создана, на это время уже назначена другая задача.\n",
                              subTask.getTitle());
        } else {
            if (epics.containsKey(idEpic)) {
                subTask.setId(getNextId());
                subTask.setIdEpic(idEpic);
                if (subTask.getStatus() == null) {
                    subTask.setStatus(Status.NEW);
                }
                epics.get(idEpic).addIdSubTask(subTask.getId());
                subTasks.put(subTask.getId(), subTask);
                changeStatus(idEpic);
                prioritizedTasks.add(subTask);
            } else {
                System.out.printf("Подзадача не создана, отсутствует Epic с id = %d.\n", idEpic);
            }
        }
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setId(getNextId());
        if ((epic.getStatus() == null) || (epic.getIdSubTasks() == null)) {
            epic = new Epic(epic);
        }
        epics.put(epic.getId(), epic);
        changeStatus(epic.getId());
        return epic.getId();
    }

    @Override
    public void updateTask(Task task) {
        if (isIntersectionTasks(getPrioritizedTasks(), task)) {
            System.out.printf("Задача, %s, не может быть изменена, на это время уже назначена другая задача.\n",
                              task.getTitle());
        } else {
            tasks.put(task.getId(), task);
            if (prioritizedTasks.contains(task)) {
                prioritizedTasks.remove(tasks.get(task.getId()));
            }
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (isIntersectionTasks(getPrioritizedTasks(), subTask)) {
            System.out.printf("Задача, %s, не может быть изменена, на это время уже назначена другая задача.\n",
                              subTask.getTitle());
        } else {
            int idSubTasks = subTask.getId();
            subTasks.put(idSubTasks, subTask);
            if (prioritizedTasks.contains(subTask)) {
                prioritizedTasks.remove(subTasks.get(idSubTasks));
            }
            prioritizedTasks.add(subTask);
            int idEpic = subTask.getIdEpic();
            if (!epics.get(idEpic).getIdSubTasks().contains(idSubTasks)) {
                epics.get(idEpic).addIdSubTask(idSubTasks);
            }
            changeStatus(subTask.getIdEpic());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        changeStatus(epic.getId());
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
            Managers.getDefaultHistory().remove(id);
        }
    }

    @Override
    public void deleteSubTask(int id) {
        if (subTasks.containsKey(id)) {
            prioritizedTasks.remove(subTasks.get(id));
            int idEpic = subTasks.get(id).getIdEpic();
            epics.get(idEpic).getIdSubTasks().remove((Integer) id);
            subTasks.remove(id);
            changeStatus(idEpic);
            Managers.getDefaultHistory().remove(id);
        }
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            int idSubTask;
            for (SubTask subTask : getSubTasksByEpicId(id)) {
                idSubTask = subTask.getId();
                prioritizedTasks.remove(subTasks.get(idSubTask));
                subTasks.remove(idSubTask);
                Managers.getDefaultHistory().remove(idSubTask);
            }
            epics.remove(id);
            Managers.getDefaultHistory().remove(id);
        }
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
        calculatingEndTimeOfEpic(id);
    }

    private int getNextId() {
        return id++;
    }

    private void deleteHistory(Set<Integer> keys) {
        for (int key : keys) {
            Managers.getDefaultHistory().remove(key);
        }
    }

    private void calculatingEndTimeOfEpic(int id) {
        List<SubTask> subTasksEpic = getSubTasksByEpicId(id);
        if (!subTasksEpic.isEmpty()) {
            LocalDateTime startEpic = null;
            LocalDateTime endEpic = null;
            Epic epic = epics.get(id);
            for (SubTask subTask : subTasksEpic) {
                if (subTask.getStartTime() == null) {
                    startEpic = null;
                    endEpic = null;
                    break;
                } else {
                    if ((startEpic == null) || (subTask.getStartTime().isBefore(startEpic))) {
                        startEpic = subTask.getStartTime();
                    }
                    if ((endEpic == null) || (subTask.getEndTime().isAfter(endEpic))) {
                        endEpic = subTask.getEndTime();
                    }
                }
            }
            epic.setEndTime(endEpic);
            epic.setStartTime(startEpic);
            epic.setDuration();
        }
    }

    private boolean isIntersectionTasks(List<Task> tasks, Task task) {
            for (Task value : tasks) {
                if (task.isIntersectionTasks(value)) {
                    return true;
                }
            }
            return false;
        }
}
