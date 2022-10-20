package controller;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest  {

    private HttpTaskServer httpTaskServer;
    private KVServer kvServer;
    private HTTPTaskManager manager;

    @BeforeEach
    public void beginTest() {
        try {
            httpTaskServer = new HttpTaskServer();
            httpTaskServer.start();
            kvServer = new KVServer();
            kvServer.start();
            manager = new HTTPTaskManager(Managers.PATH_SERVER + KVServer.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void endTest() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void createTest() {
        assertEquals(0, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(0, manager.getEpics().size(), "Неверное количество задач.");
        Task task = new Task("Задача", "Тест", null, 0);
        manager.createTask(task);
        Epic epic = new Epic("Эпик", "Тест");
        int idEpic = manager.createEpic(epic);
        SubTask subTask = new SubTask("Подзадача", "Тест", null, 0);
        manager.createSubTask(subTask, idEpic);
        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getEpics().size(), "Неверное количество задач.");
    }

    @Test
    public void getTest() {
        Task task = new Task("Задача", "Тест", null, 0);
        manager.createTask(task);
        int idTask = task.getId();
        Epic epic = new Epic("Эпик", "Тест");
        int idEpic = manager.createEpic(epic);
        SubTask subTask = new SubTask("Подзадача", "Тест", null, 0);
        manager.createSubTask(subTask, idEpic);
        int idSubTask = subTask.getId();
        assertEquals(idTask, manager.getTask(idTask).getId(), "Задача отличается.");
        assertEquals(idSubTask, manager.getSubTask(idSubTask).getId(), "Подзадача отличается.");
        assertEquals(idEpic, manager.getEpic(idEpic).getId(), "Эпик отличается.");
        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getEpics().size(), "Неверное количество задач.");
    }

    @Test
    public void deleteTest() {
        Task task = new Task("Задача", "Тест", null, 0);
        manager.createTask(task);
        int idTask = task.getId();
        Epic epic = new Epic("Эпик", "Тест");
        int idEpic = manager.createEpic(epic);
        SubTask subTask = new SubTask("Подзадача", "Тест", null, 0);
        manager.createSubTask(subTask, idEpic);
        int idSubTask = subTask.getId();
        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getEpics().size(), "Неверное количество задач.");
        manager.deleteTasks();
        manager.deleteSubTasks();
        manager.deleteEpics();
        assertEquals(0, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(0, manager.getEpics().size(), "Неверное количество задач.");
        manager.updateTask(task);
        manager.updateEpic(epic);
        manager.updateSubTask(subTask);
        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(1, manager.getEpics().size(), "Неверное количество задач.");
        manager.deleteTask(idTask);
        manager.deleteSubTask(idSubTask);
        manager.deleteEpic(idEpic);
        assertEquals(0, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, manager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(0, manager.getEpics().size(), "Неверное количество задач.");
    }

    @Test
    public void updateTest() {
        Task task = new Task("Задача", "Тест", null, 0);
        manager.createTask(task);
        int idTask = task.getId();
        Epic epic = new Epic("Эпик", "Тест");
        int idEpic = manager.createEpic(epic);
        SubTask subTask = new SubTask("Подзадача", "Тест", null, 0);
        manager.createSubTask(subTask, idEpic);
        int idSubTask = subTask.getId();
        assertEquals("Задача", manager.getTask(idTask).getTitle(), "Задача отличается.");
        assertEquals("Подзадача", manager.getSubTask(idSubTask).getTitle(), "Подзадача отличается.");
        assertEquals("Эпик", manager.getEpic(idEpic).getTitle(), "Эпик отличается.");
        Task taskNew = new Task(task.getId(), "Задача новая", task.getTitle(),
                                task.getStatus(), task.getStartTime(), task.getDuration());
        Epic epicNew = new Epic(epic.getId(), "Эпик новый", epic.getTitle(),
                                epic.getStatus(), epic.getStartTime(), epic.getDuration(), epic.getIdSubTasks());
        SubTask subTaskNew = new SubTask(subTask.getId(), "Подзадача новая", subTask.getTitle(),
                subTask.getStatus(), subTask.getStartTime(), subTask.getDuration(), subTask.getIdEpic());
        manager.updateTask(taskNew);
        manager.updateEpic(epicNew);
        manager.updateSubTask(subTaskNew);
        assertEquals("Задача новая", manager.getTask(idTask).getTitle(), "Задача не обновилась.");
        assertEquals("Подзадача новая", manager.getSubTask(idSubTask).getTitle(),
                     "Подзадача не обновилась.");
        assertEquals("Эпик новый", manager.getEpic(idEpic).getTitle(), "Эпик не обновился.");
    }
}
