import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.HttpTaskServer;
import controller.TypeAdapterForLocalDateTime;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();

//        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()
//                .serializeNulls()
//                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
//                .create();
//
//        LocalDateTime data = LocalDateTime.now();
//        String dataStr = gson.toJson(data);
//        System.out.println(dataStr);
//        LocalDateTime dataNew = gson.fromJson(dataStr, LocalDateTime.class);
//        System.out.println(dataNew);
    }
}
