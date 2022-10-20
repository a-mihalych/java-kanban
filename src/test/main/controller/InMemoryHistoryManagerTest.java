package controller;

import controller.interfaces.HistoryManager;
import controller.manager.InMemoryHistoryManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    private static HistoryManager historyManager;

    @BeforeEach
    public void newHistoryManager() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void addTaskToHistoryAndGetListTasksFromHistory(){
        assertEquals(0, historyManager.getHistory().size(), "История не пустая");
        Task task = new Task(1, "Задача", "Тест", Status.NEW, null, 0);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Размер списка не совпадает");
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Размер списка не совпадает");
    }

    @Test
    public void removeIdFromHistory() {
        assertEquals(0, historyManager.getHistory().size(), "История не пустая");
        historyManager.remove(1);
        assertEquals(0, historyManager.getHistory().size(), "История не пустая");
        Task task1 = new Task(1, "Задача1", "Тест", Status.NEW, null, 0);
        Task task2 = new Task(2, "Задача2", "Тест", Status.NEW, null, 0);
        Task task3 = new Task(3, "Задача3", "Тест", Status.NEW, null, 0);
        Task task4 = new Task(4, "Задача3", "Тест", Status.NEW, null, 0);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        assertEquals(4, historyManager.getHistory().size(), "Размер списка не совпадает");
        int id1 = historyManager.getHistory().get(0).getId();
        int id2 = historyManager.getHistory().get(1).getId();
        historyManager.remove(id1);
        assertEquals(3, historyManager.getHistory().size(), "Размер списка не совпадает");
        assertEquals(id2, historyManager.getHistory().get(0).getId(), "Задачи не совпадают");
        id2 = historyManager.getHistory().get(1).getId();
        int id3 = historyManager.getHistory().get(2).getId();
        historyManager.remove(id2);
        assertEquals(2, historyManager.getHistory().size(), "Размер списка не совпадает");
        assertEquals(id3, historyManager.getHistory().get(1).getId(), "Задачи не совпадают");
        id1 = historyManager.getHistory().get(0).getId();
        historyManager.remove(id3);
        assertEquals(1, historyManager.getHistory().size(), "Размер списка не совпадает");
        assertEquals(id1, historyManager.getHistory().get(0).getId(), "Задачи не совпадают");
        historyManager.remove(id1);
        assertEquals(0, historyManager.getHistory().size(), "Размер списка не совпадает");
    }
}
