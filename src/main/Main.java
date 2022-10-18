import controller.HttpTaskServer;
import controller.KVServer;
import controller.KVTaskClient;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//        new HttpTaskServer().start();
        new KVServer().start();
        KVTaskClient client = new KVTaskClient("http://localhost:8078");
        client.put("тест1", "один");
        client.put("тест2", "два");
        client.put("тест3", "три");
        System.out.println(client.load("тест2"));
        client.put("тест4", "четыре");
        System.out.println(client.load("тест3"));
        client.put("тест2", "Д-В-А");
        System.out.println(client.load("тест2"));
    }
}
