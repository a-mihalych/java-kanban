package controller;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    @BeforeEach
    public void beginTest() {
        taskManager.deleteTasks();
        taskManager.deleteSubTasks();
        taskManager.deleteEpics();
    }

    @Test
    public void getTaskCollectionNotNull() {
        Collection<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Список не возвращается.");
    }
    @Test
    public void getSubTaskCollectionNotNull() {
        Collection<SubTask> subTasks = taskManager.getSubTasks();
        assertNotNull(subTasks, "Список не возвращается.");
    }
    @Test
    public void getEpicCollectionNotNull() {
        Collection<Task> epics = taskManager.getTasks();
        assertNotNull(epics, "Список не возвращается.");
    }

    @Test
    public void deleteTasksCollectionEmpty() {
        taskManager.deleteTasks();
        assertTrue(taskManager.getTasks().isEmpty());
        taskManager.createTask(new Task("Задача", "Тест", null, 0));
        assertFalse(taskManager.getTasks().isEmpty());
        taskManager.deleteTasks();
        assertTrue(taskManager.getTasks().isEmpty());
    }
    @Test
    public void deleteSubTasksAndEpicCollectionEmpty() {
        taskManager.deleteSubTasks();
        taskManager.deleteEpics();
        assertTrue(taskManager.getSubTasks().isEmpty());
        assertTrue(taskManager.getEpics().isEmpty());
        Epic epic = new Epic("Эпик", "Тест");
        SubTask subTask = new SubTask("Подзадача", "Тест", null, 0);
        int idEpic = taskManager.createEpic(epic);
        taskManager.createSubTask(subTask, idEpic);
        assertFalse(taskManager.getSubTasks().isEmpty());
        assertFalse(taskManager.getEpics().isEmpty());
        taskManager.deleteSubTasks();
        taskManager.deleteEpics();
        assertTrue(taskManager.getSubTasks().isEmpty());
        assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    public void getTaskById() {
        Task task = new Task(1, "Задача", "Тест", Status.NEW, null, 0);
        assertNull(taskManager.getTask(1), "Задачи в списке нет.");
        taskManager.createTask(task);
        int id = task.getId();
        assertEquals(id, taskManager.getTask(id).getId(), "Вернулась другая задача");
        assertNull(taskManager.getTask(id + 1), "Задачи в списке нет.");
    }
    @Test
    public void getSubTaskAndEpicById() {
        Epic epic = new Epic(1, "Эпик", "Тест",
                             Status.NEW, null, 0, new ArrayList<>());
        SubTask subTask = new SubTask(2, "Подзадача", "Тест",
                                      Status.NEW, null, 0, 2);
        assertNull(taskManager.getEpic(1), "Эпика в списке нет.");
        assertNull(taskManager.getSubTask(2), "Подзадачи в списке нет.");
        int idEpic = taskManager.createEpic(epic);
        taskManager.createSubTask(subTask, idEpic);
        int idSubTask = subTask.getId();
        assertEquals(idEpic, taskManager.getEpic(idEpic).getId(), "Вернулся другой эпик");
        assertEquals(idSubTask, taskManager.getSubTask(idSubTask).getId(), "Вернулась другая подзадача");
        assertNull(taskManager.getEpic(idEpic + 1), "Эпика в списке нет.");
        assertNull(taskManager.getSubTask(idSubTask + 1), "Подзадачи в списке нет.");
    }

    @Test
    public void createNewTask() {
        Task task = new Task("Задача", "Тест", null, 0);
        taskManager.createTask(task);
        int id = task.getId();
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(id, taskManager.getTask(id).getId(), "Задачи не совпадают.");
    }
    @Test
    public void createNewSubTaskAndEpic() {
        Epic epic = new Epic("Эпик", "Тест");
        SubTask subTask = new SubTask("Подзадача", "Тест", null, 0);
        int idEpic = taskManager.createEpic(epic);
        taskManager.createSubTask(subTask, idEpic);
        int idSubTask = subTask.getId();
        assertEquals(1, taskManager.getSubTasks().size(), "Неверное количество задач.");
        assertEquals(idSubTask, taskManager.getSubTask(idSubTask).getId(), "Задачи не совпадают.");
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(idEpic, taskManager.getEpic(idEpic).getId(), "Задачи не совпадают.");
    }

    @Test
    public void updatingTaskNewTaskSameId() {
        Task task1 = new Task("Задача", "Начальная", null, 0);
        taskManager.createTask(task1);
        int id = task1.getId();
        Task task2 = new Task(id, "Задача", "Обновлённая",
                              task1.getStatus(), null, 0);
        assertEquals(id, taskManager.getTask(id).getId(), "Задачи не совпадают.");
        assertEquals("Начальная", taskManager.getTask(id).getDescription(), "Задачи не совпадают.");
        taskManager.updateTask(task2);
        assertEquals(id, taskManager.getTask(id).getId(), "Задачи не совпадают.");
        assertEquals("Обновлённая", taskManager.getTask(id).getDescription(), "Задача не обновилась.");
        Task task3 = new Task(id + 1, "Задача", "Ошибочная", Status.NEW, null, 0);
        taskManager.updateTask(task3);
        assertEquals(id + 1, taskManager.getTask(id + 1).getId(), "Задачи не совпадают.");
        assertEquals("Ошибочная", taskManager.getTask(id + 1).getDescription(),
                     "Задача не обновилась.");
    }
    @Test
    public void updatingSubTaskAndEpicNewSubTaskAndEpicSameId() {
        Epic epic1 = new Epic("Эпик", "Начальная");
        SubTask subTask1 = new SubTask("Подзадача", "Начальная", null, 0);
        int idEpic = taskManager.createEpic(epic1);
        taskManager.createSubTask(subTask1, idEpic);
        int idSubTask = subTask1.getId();
        assertEquals(idSubTask, taskManager.getSubTask(idSubTask).getId(), "Задачи не совпадают.");
        assertEquals(idEpic, taskManager.getEpic(idEpic).getId(), "Задачи не совпадают.");
        assertEquals("Начальная", taskManager.getSubTask(idSubTask).getDescription(),
                     "Задачи не совпадают.");
        assertEquals("Начальная", taskManager.getEpic(idEpic).getDescription(),
                     "Задачи не совпадают.");
        Epic epic2 = new Epic(idEpic, "Эпик", "Обновлённая",
                              epic1.getStatus(), null, 0, epic1.getIdSubTasks());
        SubTask subTask2 = new SubTask(idSubTask, "Подзадача", "Обновлённая",
                                       subTask1.getStatus(), null, 0, idEpic);
        taskManager.updateEpic(epic2);
        taskManager.updateSubTask(subTask2);
        assertEquals(idSubTask, taskManager.getSubTask(idSubTask).getId(), "Задачи не совпадают.");
        assertEquals(idEpic, taskManager.getEpic(idEpic).getId(), "Задачи не совпадают.");
        assertEquals("Обновлённая", taskManager.getSubTask(idSubTask).getDescription(),
                     "Задача не обновилась.");
        assertEquals("Обновлённая", taskManager.getEpic(idEpic).getDescription(),
                     "Задача не обновилась.");
        Epic epic3 = new Epic(3, "Эпик", "Ошибочная",
                              Status.NEW, null, 0, new ArrayList<>());
        SubTask subTask3 = new SubTask(4, "Подзадача", "Ошибочная",
                                       Status.NEW, null, 0, 3);
        taskManager.updateEpic(epic3);
        epic3.addIdSubTask(4);
        taskManager.updateSubTask(subTask3);
        taskManager.updateEpic(epic3);
        assertEquals(4, taskManager.getSubTask(4).getId(), "Задачи не совпадают.");
        assertEquals(3, taskManager.getEpic(3).getId(), "Задачи не совпадают.");
        assertEquals("Ошибочная", taskManager.getSubTask(4).getDescription(),
                     "Задача не обновилась.");
        assertEquals("Ошибочная", taskManager.getEpic(3).getDescription(),
                     "Задача не обновилась.");
    }

    @Test
    public void deleteTaskById() {
        taskManager.deleteTask(1);
        Task task = new Task("Задача", "Тест", null, 0);
        taskManager.createTask(task);
        int id = task.getId();
        assertEquals(id, taskManager.getTask(id).getId(), "Задачи не совпадают.");
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        taskManager.deleteTask(id + 1);
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        taskManager.deleteTask(id);
        assertEquals(0, taskManager.getTasks().size(), "Неверное количество задач.");
    }
    @Test
    public void deleteSubTaskAndEpicById() {
        taskManager.deleteSubTask(1);
        taskManager.deleteEpic(1);
        Epic epic = new Epic("Эпик", "Тест");
        SubTask subTask = new SubTask("Подзадача", "Тест", null, 0);
        int idEpic = taskManager.createEpic(epic);
        taskManager.createSubTask(subTask, idEpic);
        int idSubTask = subTask.getId();
        assertEquals(idEpic, taskManager.getEpic(idEpic).getId(), "Задачи не совпадают.");
        assertEquals(idSubTask, taskManager.getSubTask(idSubTask).getId(), "Задачи не совпадают.");
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getSubTasks().size(), "Неверное количество задач.");
        taskManager.deleteSubTask(idSubTask + 1);
        taskManager.deleteEpic(idEpic + 1);
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getSubTasks().size(), "Неверное количество задач.");
        taskManager.deleteSubTask(idSubTask);
        taskManager.deleteEpic(idEpic);
        assertEquals(0, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubTasks().size(), "Неверное количество задач.");
    }
}
