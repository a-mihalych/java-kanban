import controller.HistoryManager;
import controller.Managers;
import controller.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTask();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("Полить комнатные расстения", "Полить кактус и герань");
        taskManager.createTask(task1);

        Task task2 = new Task("Помыть посуду", "Вымыть тарелку, ложку и чашку");
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Закончить спринт", "Изучить теорию, выполнить и сдать финальную работу");
        SubTask subTask1 = new SubTask("Изучить теорию", "Освоить теорию, сделать задания в тренажёре");
        SubTask subTask2 = new SubTask("Сделать финальную работу", "Написать финальную работу");
        SubTask subTask3 = new SubTask("Сдать финальную работу", "Сдать финальную работу");
        int idEpic = taskManager.createEpic(epic1);
        taskManager.createSubTask(subTask1, idEpic);
        taskManager.createSubTask(subTask2, idEpic);
        taskManager.createSubTask(subTask3, idEpic);

        Epic epic2 = new Epic("Сходить за покупками", "Сходить в продуктовый магазин");
        taskManager.createEpic(epic2);

        System.out.println(historyManager.getHistory());
        taskManager.getTask(task2.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getTask(task1.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getTask(task2.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getTask(task1.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getTask(task1.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getTask(task2.getId());
        System.out.println(historyManager.getHistory());
        taskManager.deleteTask(task1.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getEpic(epic1.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getSubTask(subTask2.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getSubTask(subTask1.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getEpic(epic2.getId());
        System.out.println(historyManager.getHistory());
        taskManager.deleteEpic(epic1.getId());
        System.out.println(historyManager.getHistory());
    }
}
