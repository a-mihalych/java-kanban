package controller.manager;

import controller.interfaces.HistoryManager;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node<Task>> historyViews = new HashMap<>();
    private Node<Task> head = null;
    private Node<Task> tail = null;

    @Override
    public void add(Task task) {
        Node<Task> node = linkLast(task);
        if (historyViews.containsKey(task.getId())) {
            remove(task.getId());
        }
        historyViews.put(task.getId(), node);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (historyViews.containsKey(id)) {
            removeNode(historyViews.get(id));
            historyViews.remove(id);
        }
    }

    private Node<Task> linkLast(Task task) {
        Node<Task> node = new Node<>(tail, null, task);
        if (historyViews.isEmpty()) {
            head = node;
        } else {
            tail.setNext(node);
        }
        tail = node;
        return node;
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> node = tail;
        while (node != null) {
            tasks.add(node.getDate());
            node = node.getPrev();
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            head = node.getNext();
        }
        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            tail = node.getPrev();
        }
    }
}
