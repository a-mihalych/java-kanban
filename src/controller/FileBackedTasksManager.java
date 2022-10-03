package controller;

import model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private static final String SPLITTER = ",";
    private File file;

    private FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {

        String pathProjectDir = System.getProperty("user.dir");
        File pathFile = new File(pathProjectDir + File.separator
                + "src" + File.separator
                + "resources", "tasks.csv");

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(pathFile);

        Task task1 = new Task("Полить комнатные расстения", "Полить кактус и герань",
                              LocalDateTime.now(), 5);
        fileBackedTasksManager.createTask(task1);

        Task task2 = new Task("Помыть посуду", "Вымыть тарелку ложку чашку",
                              LocalDateTime.now().minus(Duration.ofSeconds(6000)), 11);
        fileBackedTasksManager.createTask(task2);

        Epic epic1 = new Epic("Закончить спринт", "Изучить теорию. Выполнить и сдать финальную работу");
        SubTask subTask1 = new SubTask("Изучить теорию", "Освоить теорию и сделать задания в тренажёре",
                                       LocalDateTime.now(), 410);
        SubTask subTask2 = new SubTask("Сделать финальную работу", "Написать финальную работу",
                                       LocalDateTime.now().plus(Duration.ofSeconds(10000)), 210);
        SubTask subTask3 = new SubTask("Сдать финальную работу", "Сдать финальную работу",
                                       LocalDateTime.now().plus(Duration.ofSeconds(25000)), 99);
        int idEpic = fileBackedTasksManager.createEpic(epic1);
        fileBackedTasksManager.createSubTask(subTask1, idEpic);
        fileBackedTasksManager.createSubTask(subTask2, idEpic);
        fileBackedTasksManager.createSubTask(subTask3, idEpic);

        Epic epic2 = new Epic("Сходить за покупками", "Сходить в продуктовый магазин");
        fileBackedTasksManager.createEpic(epic2);


        if (task2.getId() != 0) {
            fileBackedTasksManager.getTask(task2.getId());
        }
        if (task1.getId() != 0) {
            fileBackedTasksManager.getTask(task1.getId());
        }
        if (task2.getId() != 0) {
            fileBackedTasksManager.getTask(task2.getId());
        }
        if (epic2.getId() != 0) {
            fileBackedTasksManager.getEpic(epic2.getId());
        }
        if (subTask2.getId() != 0) {
            fileBackedTasksManager.getSubTask(subTask2.getId());
        }
        if (epic2.getId() != 0) {
            fileBackedTasksManager.getEpic(epic2.getId());
        }

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(pathFile);

        if (epic1.getId() != 0) {
            fileBackedTasksManager2.getEpic(epic1.getId());
        }
        if (subTask1.getId() != 0) {
            fileBackedTasksManager2.getSubTask(subTask1.getId());
        }
        if (task1.getId() != 0) {
            fileBackedTasksManager2.getTask(task1.getId());
        }
        if (task1.getId() != 0) {
            task1.setDuration(1111);
            fileBackedTasksManager2.updateTask(task1);
        }

        System.out.println(fileBackedTasksManager2.getPrioritizedTasks());
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
                                epics.get(((SubTask) task).getIdEpic()).addIdSubTask(task.getId());
                                break;
                            case TASK:
                                tasks.put(task.getId(), task);
                                break;
                        }
                        i++;
                    }
                    fileBackedTasksManager.setTasks(tasks);
                    fileBackedTasksManager.setSubTasks(subTasks);
                    fileBackedTasksManager.setEpics(epics);
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

    private void save() {
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

    @Override
    public void setTasks(Map<Integer, Task> tasks) {
        super.setTasks(tasks);
        save();
    }

    @Override
    public void setSubTasks(Map<Integer, SubTask> subTasks) {
        super.setSubTasks(subTasks);
        save();
    }

    @Override
    public void setEpics(Map<Integer, Epic> epics) {
        super.setEpics(epics);
        save();
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
        if (isIntersectionTasks(getPrioritizedTasks(), task)) {
            System.out.printf("Задача, %s, не может быть создана, на это время уже назначена другая задача.\n",
                              task.getTitle());
        } else {
            super.createTask(task);
            save();
        }
    }

    @Override
    public void createSubTask(SubTask subTask, int idEpic) {
        if (isIntersectionTasks(getPrioritizedTasks(), subTask)) {
            System.out.printf("Задача, %s, не может быть создана, на это время уже назначена другая задача.\n",
                              subTask.getTitle());
        } else {
            super.createSubTask(subTask, idEpic);
            save();
        }
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        if (isIntersectionTasks(getPrioritizedTasks(), task)) {
            System.out.printf("Задача, %s, не может быть изменена, на это время уже назначена другая задача.\n",
                              task.getTitle());
        } else {
            super.updateTask(task);
            save();
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (isIntersectionTasks(getPrioritizedTasks(), subTask)) {
            System.out.printf("Задача, %s, не может быть изменена, на это время уже назначена другая задача.\n",
                              subTask.getTitle());
        } else {
            super.updateSubTask(subTask);
            save();
        }
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

    private boolean isIntersectionTasks(List<Task> tasks, Task task) {
        for (Task value : tasks) {
            if (task.isIntersectionTasks(value)) {
                return true;
            }
        }
        return false;
    }
}
