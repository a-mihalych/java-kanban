package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.adapter.TypeAdapterForLocalDateTime;
import controller.manager.Managers;
import controller.server.HttpTaskServer;
import controller.server.KVServer;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;
    private KVServer kvServer;
    private HttpClient client;

    @BeforeEach
    public void beginTest() {
        try {
            httpTaskServer = new HttpTaskServer();
            httpTaskServer.start();
            kvServer = new KVServer();
            kvServer.start();
            client = HttpClient.newHttpClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/subtask");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @AfterEach
    public void endTest() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void getTasksTest() {
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getTaskTest() {
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getTaskidTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Task task = new Task(1, "Задача", "Тест", Status.NEW, null, 0);
        String json = gson.toJson(task);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }

        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/task?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getSubtaskTest() {
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getSubtaskidTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Epic epic = new Epic(1, "Эпик", "Тест", Status.NEW, null, 0, new ArrayList<>());
        String json = gson.toJson(epic);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }

        SubTask subTask = new SubTask(2, "Подзадача", "Тест",
                Status.NEW, null, 0, 1);
        json = gson.toJson(subTask);
        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/subtask");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }

        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/subtask?id=2");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getEpicTest() {
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getEpicidTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Epic epic = new Epic(1, "Эпик", "Тест", Status.NEW, null, 0, new ArrayList<>());
        String json = gson.toJson(epic);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }

        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void getHistoryTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Task task = new Task(1, "Задача", "Тест", Status.NEW, null, 0);
        String json = gson.toJson(task);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }

        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/task?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }

        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/history");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void postTaskCreateTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Task task = new Task("Задача", "Тест", null, 0);
        String json = gson.toJson(task);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(201, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }
    }

    @Test
    public void postTaskUpdateTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Task task = new Task(1, "Задача", "Тест", Status.NEW, null, 0);
        String json = gson.toJson(task);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(201, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }
    }

    @Test
    public void postSubtaskTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Epic epic = new Epic(1, "Эпик", "Тест", Status.NEW, null, 0, new ArrayList<>());
        String json = gson.toJson(epic);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }

        SubTask subTask = new SubTask(2, "Подзадача", "Тест",
                Status.NEW, null, 0, 1);
        json = gson.toJson(subTask);
        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/subtask");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(201, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }
    }

    @Test
    public void postSubtaskidTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Epic epic = new Epic(1, "Эпик", "Тест", Status.NEW, null, 0, new ArrayList<>());
        String json = gson.toJson(epic);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }

        SubTask subTask = new SubTask("Подзадача", "Тест", null, 0);
        json = gson.toJson(subTask);
        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/subtask?id=1");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(201, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }
    }

    @Test
    public void postEpicCreateTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Epic epic = new Epic("Эпик", "Тест");
        String json = gson.toJson(epic);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(201, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }
    }

    @Test
    public void postEpicUpdateTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Epic epic = new Epic(1, "Эпик", "Тест", Status.NEW, null, 0, new ArrayList<>());
        String json = gson.toJson(epic);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(201, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }
    }

    @Test
    public void deleteTaskTest() {
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void deleteTaskidTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Task task = new Task(1, "Задача", "Тест", Status.NEW, null, 0);
        String json = gson.toJson(task);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }

        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/task?id=1");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void deleteSubtaskTest() {
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void deleteSubtaskidTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Epic epic = new Epic(1, "Эпик", "Тест", Status.NEW, null, 0, new ArrayList<>());
        String json = gson.toJson(epic);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }

        SubTask subTask = new SubTask(2, "Подзадача", "Тест",
                Status.NEW, null, 0, 1);
        json = gson.toJson(subTask);
        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/subtask");
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }

        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/subtask?id=2");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void deleteEpicTest() {
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }

    @Test
    public void deleteEpicidTest() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        Epic epic = new Epic(1, "Эпик", "Тест", Status.NEW, null, 0, new ArrayList<>());
        String json = gson.toJson(epic);
        URI uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }

        uri = URI.create(Managers.PATH_SERVER + HttpTaskServer.PORT + "/tasks/epic?id=1");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(204, response.statusCode(), "Неверный ответ.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Запрос провалился.");
        }
    }
}
