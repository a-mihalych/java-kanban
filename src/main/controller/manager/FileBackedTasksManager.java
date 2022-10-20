package controller.manager;

import controller.exception.ManagerSaveException;
import controller.interfaces.HistoryManager;
import controller.interfaces.TaskManager;
import model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private static final String SPLITTER = ",";
    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private static String historyToString(HistoryManager manager) {
        List<Task> historyViews = manager.getHistory();
        StringBuilder lineViews = new StringBuilder("\n");
        for (Task task : historyViews) {
            lineViews.append(task.getId() + SPLITTER);
        }
        lineViews.setLength(lineViews.length() - 1);
        return lineViews.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> historyViews = Arrays.stream(value.split(SPLITTER))
                                    .map(Integer::parseInt).collect(Collectors.toList());
        Collections.reverse(historyViews);
        return historyViews;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        HistoryManager historyManager = Managers.getDefaultHistory();
        try {
            if (!file.isFile()) {
                file.createNewFile();
            } else {
                String fileLine = Files.readString(Path.of(String.valueOf(file)));
                String[] fileLines = fileLine.split("\n");
                if (fileLines.length > 1) {
                    int i = 1;
                    Map<Integer, Epic> epics = new HashMap<>();
                    Map<Integer, Task> tasks = new HashMap<>();
                    Map<Integer, SubTask> subTasks = new HashMap<>();
                    Task task;
                    while (i < fileLines.length && !fileLines[i].isBlank()) {
                        task = fileBackedTasksManager.fromString(fileLines[i]);
                        switch (TypeTask.valueOf(task.getClass().getSimpleName().toUpperCase())) {
                            case EPIC:
                                epics.put(task.getId(), (Epic) task);
                                break;
                            case SUBTASK:
                                subTasks.put(task.getId(), (SubTask) task);
                                break;
                            case TASK:
                                tasks.put(task.getId(), task);
                                break;
                        }
                        i++;
                    }
                    fileBackedTasksManager.deleteTasks();
                    fileBackedTasksManager.deleteSubTasks();
                    fileBackedTasksManager.deleteEpics();
                    fileBackedTasksManager.setTasks(tasks);
                    fileBackedTasksManager.setEpics(epics);
                    fileBackedTasksManager.setSubTasks(subTasks);
                    fileBackedTasksManager.addPrioritizedTasks();
                    if (fileLines.length == (i + 2)) {
                        List<Integer> historyViews = historyFromString(fileLines[i + 1]);
                        for (int id : historyViews) {
                            if (epics.containsKey(id)) {
                                historyManager.add(epics.get(id));
                            } else {
                                if (subTasks.containsKey(id)) {
                                    historyManager.add(subTasks.get(id));
                                } else {
                                    historyManager.add(tasks.get(id));
                                }
                            }
                        }
                        fileBackedTasksManager.save();
                    }
                    fileBackedTasksManager.restoreId();
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Чтение данных из файла было прервано.");
        }
        return fileBackedTasksManager;
    }

    protected void save() {
        String headFile = "id,type,name,status,description,startTime,duration,epic\n";
        StringBuilder stringForFile = new StringBuilder(headFile);
        for(Task task : getTasks()) {
            stringForFile.append(toString(task));
        }
        for(Epic epic : getEpics()) {
            stringForFile.append(toString(epic));
        }
        for(SubTask subTask : getSubTasks()) {
            stringForFile.append(toString(subTask));
        }
        stringForFile.append(historyToString(Managers.getDefaultHistory()));
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write(String.valueOf(stringForFile));
        } catch (IOException e) {
            throw new ManagerSaveException("Сохранение данных в файл было прервано.");
        }
    }

    private String toString(Task task) {
        TypeTask typeTask = null;
        String idEpic = "";
        switch (TypeTask.valueOf(task.getClass().getSimpleName().toUpperCase())) {
            case EPIC:
                typeTask = TypeTask.EPIC;
                break;
            case SUBTASK:
                typeTask = TypeTask.SUBTASK;
                idEpic += ((SubTask) task).getIdEpic();
                break;
            case TASK:
                typeTask = TypeTask.TASK;
                break;
        }
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s\n",
                             task.getId(), typeTask, task.getTitle(), task.getStatus(), task.getDescription(),
                             task.getStartTimeLine(), task.getDurationHoursMinutesLine(), idEpic);
    }

    private Task fromString(String value) {
        Task task = null;
        String[] taskLines = value.split(SPLITTER);
        int id = Integer.parseInt(taskLines[0]);
        TypeTask type = TypeTask.valueOf(taskLines[1]);
        String name = taskLines[2];
        Status status = Status.valueOf(taskLines[3]);
        String description = taskLines[4];
        LocalDateTime startTime = null;
        if (!taskLines[5].equals("null")) {
            startTime = LocalDateTime.parse(taskLines[5], Task.FORMATTER);
        }
        String[] durationLines = taskLines[6].split(":");
        int duration = Integer.parseInt(durationLines[0]) * 60 + Integer.parseInt(durationLines[1]);
        switch (type) {
            case TASK:
                task = new Task(id, name, description, status, startTime, duration);
                break;
            case EPIC:
                task = new Epic(id, name, description, status, startTime, duration, new ArrayList<Integer>());
                break;
            case SUBTASK:
                task = new SubTask(id, name, description, status, startTime, duration, Integer.parseInt(taskLines[7]));
                break;
        }
        return task;
    }

    private void setTasks(Map<Integer, Task> tasks) {
        for (Task task : tasks.values()) {
            updateTask(task);
        }
    }

    private void setSubTasks(Map<Integer, SubTask> subTasks) {
        for (SubTask subTask : subTasks.values()) {
            updateSubTask(subTask);
        }
    }

    private void setEpics(Map<Integer, Epic> epics) {
        for (Epic epic : epics.values()) {
            updateEpic(epic);
        }
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void createTask(Task task) {
            super.createTask(task);
            save();
    }

    @Override
    public void createSubTask(SubTask subTask, int idEpic) {
            super.createSubTask(subTask, idEpic);
            save();
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
            super.updateTask(task);
            save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
            super.updateSubTask(subTask);
            save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }
}
