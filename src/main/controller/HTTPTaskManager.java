package controller;

import com.google.gson.*;
import model.Epic;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;

public class HTTPTaskManager extends FileBackedTasksManager {

    public static final String KEY_TASKS = "tasks";
    public static final String KEY_SUBTASKS = "subtasks";
    public static final String KEY_EPICS = "epics";
    public static final String KEY_HISTORY = "history";
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

    public KVTaskClient getClient() {
        return client;
    }

    public void save() {
        String json = gson.toJson(getTasks());
        client.put(KEY_TASKS, json);
        json = gson.toJson(getSubTasks());
        client.put(KEY_SUBTASKS, json);
        json = gson.toJson(getEpics());
        client.put(KEY_EPICS, json);
        json = gson.toJson(Managers.getDefaultHistory().getHistory());
        client.put(KEY_HISTORY, json);
    }

    public void load() {
        HistoryManager history = Managers.getDefaultHistory();
        deleteTasks();
        deleteSubTasks();
        deleteEpics();
        JsonArray jsonArray = parserJson(KEY_TASKS);
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                updateTask(gson.fromJson(element, Task.class));
            }
        }
        jsonArray = parserJson(KEY_SUBTASKS);
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                updateSubTask(gson.fromJson(element, SubTask.class));
            }
        }
        jsonArray = parserJson(KEY_EPICS);
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                updateEpic(gson.fromJson(element, Epic.class));
            }
        }
        jsonArray = parserJson(KEY_HISTORY);
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
