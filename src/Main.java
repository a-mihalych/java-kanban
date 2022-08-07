import controller.HistoryManager;
import controller.Managers;
import controller.TaskManager;
import model.Epic;
import model.Status;
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
        SubTask subTask2 = new SubTask("Сделать финальную работу", "Написать финальную работу, сдать её");
        int idEpic = taskManager.createEpic(epic1);
        taskManager.createSubTask(subTask1, idEpic);
        taskManager.createSubTask(subTask2, idEpic);

        Epic epic2 = new Epic("Сходить за покупками", "Сходить в продуктовый магазин");
        SubTask subTask3 = new SubTask("Сходить за продуктами", "Купить продукты");
        idEpic = taskManager.createEpic(epic2);
        taskManager.createSubTask(subTask3, idEpic);

        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubTasks());

        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        task2.setStatus(Status.DONE);
        taskManager.updateTask(task2);
        subTask1.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask1);
        subTask3.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask3);
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubTasks());

        taskManager.deleteTask(task1.getId());
        taskManager.deleteEpic(epic2.getId());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubTasks());

        System.out.println(historyManager.getHistory());
        taskManager.getTask(task2.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getEpic(epic1.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getSubTask(subTask2.getId());
        System.out.println(historyManager.getHistory());
    }
}
