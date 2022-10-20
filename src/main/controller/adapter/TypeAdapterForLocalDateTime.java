package controller.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class TypeAdapterForLocalDateTime extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime != null) {
            jsonWriter.value(localDateTime.format(Task.FORMATTER));
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        String line = jsonReader.nextString();
        if ("null".equals(line)) {
            return null;
        }
        return LocalDateTime.parse(line, Task.FORMATTER);
    }
}
