import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        // Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
        ArrayList<String> description = new ArrayList<>();
        description.add("Набрать воды");
        description.add("Полить герань");
        description.add("Полить кактус");
        Task task1 = new Task("Полить комнатные расстения", description);
        manager.creationTask(task1);

        description = new ArrayList<>();
        description.add("Помыть ложку");
        description.add("Помыть чашку");
        description.add("Помыть тарелку");
        Task task2 = new Task("Помыть посуду", description);
        manager.creationTask(task2);

        description = new ArrayList<>();
        description.add("Изучить теорию");
        description.add("Выполнить финальную работу");
        Epic epic1 = new Epic("Закончить спринт", description);
        description = new ArrayList<>();
        description.add("Прочитать конспекты");
        description.add("Сделать практические задания");
        SubTask subTask1 = new SubTask(epic1.getDescription().get(0), description);
        int id = manager.creationSubTask(subTask1);
        epic1.addIdSubTask(id);
        description = new ArrayList<>();
        description.add("Изучить задание");
        description.add("Написать код");
        description.add("Сдать работу");
        SubTask subTask2 = new SubTask(epic1.getDescription().get(1), description);
        id = manager.creationSubTask(subTask2);
        epic1.addIdSubTask(id);
        id = manager.creationEpic(epic1);
        subTask1.setIdEpic(id);
        subTask2.setIdEpic(id);

        description = new ArrayList<>();
        description.add("Сходить за продуктами");
        Epic epic2 = new Epic("Сходить за покупками", description);
        description = new ArrayList<>();
        description.add("Дойти до продуктового магазина");
        description.add("Выбрать продукты");
        description.add("Вернуться домой");
        SubTask subTask3 = new SubTask(epic2.getDescription().get(0), description);
        id = manager.creationSubTask(subTask3);
        epic2.addIdSubTask(id);
        id = manager.creationEpic(epic2);
        subTask3.setIdEpic(id);

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
