import controller.FileBackedTasksManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.File;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        String pathProjectDir = System.getProperty("user.dir");
        File pathFile = new File(pathProjectDir + File.separator
                + "src" + File.separator
                + "resources", "tasks.csv");

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(pathFile);

        Task task1 = new Task("Полить комнатные расстения", "Полить кактус и герань",
                LocalDateTime.now(), 6);
        SubTask subTask1 = new SubTask("Изучить теорию", "Освоить теорию и сделать задания в тренажёре",
                LocalDateTime.now(), 951);
        Epic epic2 = new Epic("Сходить за покупками", "Сходить в продуктовый магазин");

        fileBackedTasksManager.createTask(task1);
        int idEpic = fileBackedTasksManager.createEpic(epic2);
        fileBackedTasksManager.createSubTask(subTask1, idEpic);
        System.out.println(fileBackedTasksManager.getPrioritizedTasks());
    }
}
