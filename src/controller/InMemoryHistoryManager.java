package controller;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final int MAX_HISTORY_VIEVS = 10;
    private List<Task> historyViews  = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (historyViews.size() == MAX_HISTORY_VIEVS) {
            historyViews.remove(0);
        }
        historyViews.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyViews;
    }
}
