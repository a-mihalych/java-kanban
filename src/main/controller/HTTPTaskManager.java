package controller;

import com.google.gson.*;
import model.Epic;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;

public class HTTPTaskManager extends FileBackedTasksManager {

    private KVTaskClient client;
    private Gson gson;

    public HTTPTaskManager(String url) {
        super(Managers.PATH_FILE);
        this.client = new KVTaskClient(url);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
    }

    public void save() {
        String json = gson.toJson(getTasks());
        client.put("tasks", json);
        json = gson.toJson(getSubTasks());
        client.put("subtasks", json);
        json = gson.toJson(getEpics());
        client.put("epics", json);
        json = gson.toJson(Managers.getDefaultHistory().getHistory());
        client.put("history", json);
    }

    public void load() {
        HistoryManager history = Managers.getDefaultHistory();
        deleteTasks();
        deleteSubTasks();
        deleteEpics();
        JsonArray jsonArray = parserJson("tasks");
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                updateTask(gson.fromJson(element, Task.class));
            }
        }
        jsonArray = parserJson("subtasks");
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                updateSubTask(gson.fromJson(element, SubTask.class));
            }
        }
        jsonArray = parserJson("epics");
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                updateEpic(gson.fromJson(element, Epic.class));
            }
        }
        jsonArray = parserJson("history");
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                history.add(gson.fromJson(element, Task.class));
            }
        }
        addPrioritizedTasks();
        restoreId();
    }

    private JsonArray parserJson(String key) {
        String json = client.load(key);
        JsonElement jsonElement = JsonParser.parseString(json);
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            return jsonArray;
        }
        System.out.println("Список не получен");
        return null;
    }
}
