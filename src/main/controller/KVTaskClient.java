package controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    public String API_TOKEN;
    private final String path;

    public KVTaskClient(String path) {
        this.path = path;
        URI uri = URI.create(path + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = httpClient.send(request, handler);
            API_TOKEN = response.body();
            System.out.println("API_TOKEN получен");
        } catch (IOException | InterruptedException e) {
            System.out.println("API_TOKEN не получен, нужно зарегистрироваться повторно");
        }
    }

    public void put(String key, String json) {
        URI uri = URI.create(path + "/save/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpClient.newHttpClient().version();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = httpClient.send(request, handler);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Сохранение было прервано. Попробуйте ещй раз.");
        }
    }

    public String load(String key) {
        URI uri = URI.create(path + "/load/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpClient.newHttpClient().version();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = httpClient.send(request, handler);
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Получение значения было прервано. Попробуйте ещй раз.");
        }
        return null;
    }
}
