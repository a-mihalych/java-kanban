package controller;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    public InMemoryTaskManagerTest() {
        super((InMemoryTaskManager) Managers.getDefaultTask());
    }

    @Override
    @BeforeEach
    public void beginTest() {
        super.beginTest();
        taskManager.restoreId();
    }

    @Test
    public void restoreId() {
        Task task1 = new Task("Задача", "Тест1", null, 0);
        taskManager.createTask(task1);
        taskManager.restoreId();
        Task task2 = new Task("Задача", "Тест2", null, 0);
        taskManager.createTask(task2);
        assertEquals(task1.getId() + 1, task2.getId(), "Не верный id");
    }

    @Test
    public void getCollectionPrioritizedTasks () {
        Collection<Task> tasks = taskManager.getPrioritizedTasks();
        assertNotNull(tasks, "Список не возвращается.");
    }

    @Test
    public void addInCollectionPrioritizedTasks() {
        Task task = new Task("Задача", "Тест1", null, 0);
        taskManager.createTask(task);
        taskManager.addPrioritizedTasks();
        assertEquals(1, taskManager.getPrioritizedTasks().size(),
                     "Задача не добавилась в список по приоритетам.");
    }

    @Test
    public void calculationOfEpicStatus() {
        Epic epic = new Epic("Задачище", "Тест");
        int idEpic = taskManager.createEpic(epic);
        assertEquals(Status.NEW, taskManager.getEpic(idEpic).getStatus(), "Статус должен быть NEW.");
        SubTask subTask1 = new SubTask("Подзадача1", "Тест", null, 0);
        SubTask subTask2 = new SubTask("Подзадача2", "Тест", null, 0);
        taskManager.createSubTask(subTask1, idEpic);
        taskManager.createSubTask(subTask2, idEpic);
        assertEquals(Status.NEW, taskManager.getEpic(idEpic).getStatus(), "Статус должен быть NEW.");
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.updateEpic(epic);
        assertEquals(Status.DONE, taskManager.getEpic(idEpic).getStatus(), "Статус должен быть DONE.");
        subTask1.setStatus(Status.NEW);
        taskManager.updateEpic(epic);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(idEpic).getStatus(),
             "Статус должен быть IN_PROGRESS.");
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(epic);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(idEpic).getStatus(),
             "Статус должен быть IN_PROGRESS.");
    }

    @Test
    public void presenceOfEpicWhenCreatingSubtask() {
        Epic epic = new Epic("Задачище", "Тест");
        SubTask subTask = new SubTask("Подзадача", "Тест", null, 0);
        int idEpic = taskManager.createEpic(epic);
        taskManager.createSubTask(subTask, 3);
        assertEquals(0, subTask.getId(), "Подзадача создана.");
        taskManager.createSubTask(subTask, idEpic);
        assertEquals(2, subTask.getId(), "Подзадача не создана.");
    }

    @Test
    public void changingStatusOfEpicWhenAddingSubtask() {
        Epic epic = new Epic("Задачище", "Тест");
        SubTask subTask1 = new SubTask("Подзадача1", "Тест", null, 0);
        int idEpic = taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1, idEpic);
        assertEquals(Status.NEW, taskManager.getEpic(idEpic).getStatus(), "Статус должен быть NEW.");
        SubTask subTask2 = new SubTask("Подзадача1", "Тест", null, 0);
        subTask2.setStatus(Status.IN_PROGRESS);
        taskManager.createSubTask(subTask2, idEpic);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(idEpic).getStatus(),
             "Статус должен быть IN_PROGRESS.");
    }
}
