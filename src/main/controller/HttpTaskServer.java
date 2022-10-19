package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static final int PORT = 8080;
    public static final String PATH = "/tasks";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final String[] RESOURCES = {"tasks", "task", "subtask", "epic", "history"};
    private HttpServer httpServer;
    private Gson gson;
    private FileBackedTasksManager fileBackedTasksManager;

    public HttpTaskServer() throws IOException {
        fileBackedTasksManager = FileBackedTasksManager.loadFromFile(Managers.PATH_FILE);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext(PATH, this::handleTask);
    }

    private void handleTask(HttpExchange httpExchange)  throws IOException {
        try {
            System.out.println("Началась обработка запроса от клиента: " + httpExchange.getRequestURI());
            String response = "";
            int rCode = 400;
            int lengthResponse = 0;
            String method = httpExchange.getRequestMethod();
            String body = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            String resource = getResource(httpExchange.getRequestURI().getPath());
            int id = getId(httpExchange.getRequestURI().getQuery());
            String endPoint = method + resource + (id > 0 ? "id" : id == 0 ? "" : "falseid");
            if ("POST".equals(method) && body.isBlank()) {
                endPoint += "falsebody";
            }
            SubTask subTask;
            switch(endPoint) {
                case "GETtasks":
                    response = gson.toJson(fileBackedTasksManager.getTasks());
                    response += gson.toJson(fileBackedTasksManager.getSubTasks());
                    response += gson.toJson(fileBackedTasksManager.getEpics());
                    rCode = 200;
                    break;
                case "GETtask":
                    response = gson.toJson(fileBackedTasksManager.getTasks());
                    rCode = 200;
                    break;
                case "GETtaskid":
                    response = gson.toJson(fileBackedTasksManager.getTask(id));
                    rCode = 200;
                    if (response == null) {
                        response = "id=" + id + ", не найден.";
                        rCode = 404;
                    }
                    break;
                case "GETsubtask":
                    response = gson.toJson(fileBackedTasksManager.getSubTasks());
                    rCode = 200;
                    break;
                case "GETsubtaskid":
                    response = gson.toJson(fileBackedTasksManager.getSubTask(id));
                    rCode = 200;
                    if (response == null) {
                        response = "id=" + id + ", не найден.";
                        rCode = 404;
                    }
                    break;
                case "GETepic":
                    response = gson.toJson(fileBackedTasksManager.getEpics());
                    rCode = 200;
                    break;
                case "GETepicid":
                    response = gson.toJson(fileBackedTasksManager.getEpic(id));
                    rCode = 200;
                    if (response == null) {
                        response = "id=" + id + ", не найден.";
                        rCode = 404;
                    }
                    break;
                case "GEThistory":
                    response = gson.toJson(Managers.getDefaultHistory().getHistory());
                    rCode = 200;
                    break;
                case "POSTtask":
                    Task task = gson.fromJson(body, Task.class);
                    if (task.getId() == 0) {
                        fileBackedTasksManager.createTask(task);
                    } else {
                        fileBackedTasksManager.updateTask(task);
                    }
                    rCode = 201;
                    break;
                case "POSTsubtask":
                    subTask = gson.fromJson(body, SubTask.class);
                    fileBackedTasksManager.updateSubTask(subTask);
                    rCode = 201;
                    break;
                case "POSTsubtaskid":
                    subTask = gson.fromJson(body, SubTask.class);
                    fileBackedTasksManager.createSubTask(subTask, id);
                    rCode = 201;
                    break;
                case "POSTepic":
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (epic.getId() == 0) {
                        fileBackedTasksManager.createEpic(epic);
                    } else {
                        fileBackedTasksManager.updateEpic(epic);
                    }
                    rCode = 201;
                    break;
                case "DELETEtask":
                    fileBackedTasksManager.deleteTasks();
                    rCode = 204;
                    lengthResponse = -1;
                    break;
                case "DELETEtaskid":
                    fileBackedTasksManager.deleteTask(id);
                    rCode = 204;
                    lengthResponse = -1;
                    break;
                case "DELETEsubtask":
                    fileBackedTasksManager.deleteSubTasks();
                    rCode = 204;
                    lengthResponse = -1;
                    break;
                case "DELETEsubtaskid":
                    fileBackedTasksManager.deleteSubTask(id);
                    rCode = 204;
                    lengthResponse = -1;
                    break;
                case "DELETEepic":
                    fileBackedTasksManager.deleteEpics();
                    rCode = 204;
                    lengthResponse = -1;
                    break;
                case "DELETEepicid":
                    fileBackedTasksManager.deleteEpic(id);
                    rCode = 204;
                    lengthResponse = -1;
                    break;
                default:
                    System.out.println("Сервер не распознанный запрос: " + method + ":" + httpExchange.getRequestURI());
                    response = "Запрос не распознан!";
                    rCode = 400;
            }
            if (rCode >= 400) {
                httpExchange.getResponseHeaders().add("Content-Type", "text/plain");
            } else {
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            }
            httpExchange.sendResponseHeaders(rCode, lengthResponse);
            if (lengthResponse != -1) {
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes(DEFAULT_CHARSET));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановка сервера на порту " + PORT);
    }
    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Ссылка: http://localhost:" + PORT + PATH);
        httpServer.start();
    }

    private String getResource(String path) {
        String[] linePath = path.split("/");
        String resource = PATH.substring(1);
        if (linePath.length > 2) {
            resource = linePath[2];
        }
        for (String s : RESOURCES) {
            if (s.equals(resource)) {
                return s;
            }
        }
        return "";
    }

    private int getId(String idLine) {
        int id = 0;
        if (idLine != null) {
            try {
                String[] idLineSplit = idLine.split("=");
                if (idLineSplit.length > 1) {
                    id = Integer.parseInt(idLineSplit[1]);
                    if (id <= 0) {
                        id = -1;
                    }
                } else {
                    id = -1;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return id;
    }
}
