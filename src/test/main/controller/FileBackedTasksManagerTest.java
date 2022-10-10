package controller;

import model.Epic;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public static String pathProjectDir = System.getProperty("user.dir");
    public static File pathFile = new File(pathProjectDir + File.separator
                                           + "src" + File.separator
                                           + "resources", "tasks.csv");

    public FileBackedTasksManagerTest() {
        super(FileBackedTasksManager.loadFromFile(pathFile));
    }

    @Override
    @BeforeEach
    public void beginTest() {
        super.beginTest();
        taskManager.restoreId();
    }

    @Test
    public void loadFromFile() {
        assertEquals(0, taskManager.getPrioritizedTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(0, Managers.getDefaultHistory().getHistory().size(), "Неверное количество задач.");
        Task task1 = new Task("Задача", "Тест", null, 0);
        taskManager.createTask(task1);
        taskManager.getTask(task1.getId());
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(1, Managers.getDefaultHistory().getHistory().size(), "Неверное количество задач.");
        taskManager.deleteTask(task1.getId());
        assertEquals(0, taskManager.getPrioritizedTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(0, Managers.getDefaultHistory().getHistory().size(), "Неверное количество задач.");
        taskManager = null;
        taskManager = FileBackedTasksManager.loadFromFile(pathFile);
        assertEquals(0, taskManager.getPrioritizedTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(0, Managers.getDefaultHistory().getHistory().size(), "Неверное количество задач.");
        Task task2 = new Task("Задача", "Тест", null, 0);
        taskManager.createTask(task2);
        Epic epic = new Epic("Задачище", "Тест");
        taskManager.createEpic(epic);
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(0, Managers.getDefaultHistory().getHistory().size(), "Неверное количество задач.");
        taskManager = null;
        taskManager = FileBackedTasksManager.loadFromFile(pathFile);
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(0, Managers.getDefaultHistory().getHistory().size(), "Неверное количество задач.");
        taskManager.getTask(task1.getId());
        taskManager.getEpic(epic.getId());
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(2, Managers.getDefaultHistory().getHistory().size(), "Неверное количество задач.");
        taskManager = null;
        taskManager = FileBackedTasksManager.loadFromFile(pathFile);
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(2, Managers.getDefaultHistory().getHistory().size(), "Неверное количество задач.");
    }
}
