package controller;

public class Managers {

    private static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }

    public static TaskManager getDefaultTask() {
        return inMemoryTaskManager;
    }
}
