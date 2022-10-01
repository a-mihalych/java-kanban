import model.Epic;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Task task1 = new Task("Полить комнатные расстения", "Полить кактус и герань",
                LocalDateTime.now(), Duration.ofSeconds(99));
        System.out.println(task1);
        SubTask subTask1 = new SubTask("Изучить теорию", "Освоить теорию и сделать задания в тренажёре",
                LocalDateTime.now(), Duration.ofSeconds(6000));
        System.out.println(subTask1);
        Epic epic2 = new Epic("Сходить за покупками", "Сходить в продуктовый магазин",
                LocalDateTime.now().minus(Duration.ofSeconds(36000)), Duration.ofSeconds(3000));
        System.out.println(epic2);
    }
}
