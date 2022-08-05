import controller.InMemoryTaskManager;
import controller.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();

        // Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
        Task task1 = new Task("Полить комнатные расстения", "Полить кактус и герань");
        manager.createTask(task1);

        Task task2 = new Task("Помыть посуду", "Вымыть тарелку, ложку и чашку");
        manager.createTask(task2);

        Epic epic1 = new Epic("Закончить спринт", "Изучить теорию, выполнить и сдать финальную работу");
        SubTask subTask1 = new SubTask("Изучить теорию", "Освоить теорию, сделать задания в тренажёре");
        SubTask subTask2 = new SubTask("Сделать финальную работу", "Написать финальную работу, сдать её");
        int idEpic = manager.createEpic(epic1);
        manager.createSubTask(subTask1, idEpic);
        manager.createSubTask(subTask2, idEpic);

        Epic epic2 = new Epic("Сходить за покупками", "Сходить в продуктовый магазин");
        SubTask subTask3 = new SubTask("Сходить за продуктами", "Купить продукты");
        idEpic = manager.createEpic(epic2);
        manager.createSubTask(subTask3, idEpic);

        // Распечатайте списки эпиков, задач и подзадач, через System.out.println(..)
        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubTasks());

        // Измените статусы созданных объектов, распечатайте.
        // Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);
        task2.setStatus(Status.DONE);
        manager.updateTask(task2);
        subTask1.setStatus(Status.DONE);
        manager.updateSubTask(subTask1);
        subTask3.setStatus(Status.DONE);
        manager.updateSubTask(subTask3);
        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubTasks());

        // И, наконец, попробуйте удалить одну из задач и один из эпиков.
        manager.deleteTask(task1.getId());
        manager.deleteEpic(epic2.getId());
        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubTasks());
    }
}
