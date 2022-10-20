package controller;

import controller.manager.FileBackedTasksManager;
import controller.manager.Managers;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public FileBackedTasksManagerTest() {
        super(FileBackedTasksManager.loadFromFile(Managers.PATH_FILE));
    }

    @Override
    @BeforeEach
    public void beginTest() {
        super.beginTest();
        taskManager.restoreId();
    }

    private void testMapSetListForSize(int sizeTasks, int sizeSubTasks, int sizeEpics,
                                       int sizePrioritized, int sizeHistory) {
        assertEquals(sizePrioritized, taskManager.getPrioritizedTasks().size(), "Неверное количество задач.");
        assertEquals(sizeTasks, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(sizeSubTasks, taskManager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(sizeEpics, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(sizeHistory, Managers.getDefaultHistory().getHistory().size(),
                    "Неверное количество задач.");
    }

    @Test
    public void loadFromFile() {
        testMapSetListForSize(0, 0, 0, 0, 0);
        Task task1 = new Task("Задача", "Тест", null, 0);
        taskManager.createTask(task1);
        taskManager.getTask(task1.getId());
        testMapSetListForSize(1, 0, 0, 1, 1);
        taskManager.deleteTask(task1.getId());
        testMapSetListForSize(0, 0, 0, 0, 0);
        taskManager = null;
        taskManager = FileBackedTasksManager.loadFromFile(Managers.PATH_FILE);
        testMapSetListForSize(0, 0, 0, 0, 0);
        Task task2 = new Task("Задача", "Тест", null, 0);
        taskManager.createTask(task2);
        Epic epic = new Epic("Эпик", "Тест");
        taskManager.createEpic(epic);
        testMapSetListForSize(1, 0, 1, 1, 0);
        taskManager = null;
        taskManager = FileBackedTasksManager.loadFromFile(Managers.PATH_FILE);
        testMapSetListForSize(1, 0, 1, 1, 0);
        taskManager.getTask(task1.getId());
        taskManager.getEpic(epic.getId());
        testMapSetListForSize(1, 0, 1, 1, 2);
        taskManager = null;
        taskManager = FileBackedTasksManager.loadFromFile(Managers.PATH_FILE);
        testMapSetListForSize(1, 0, 1, 1, 2);
    }
}
