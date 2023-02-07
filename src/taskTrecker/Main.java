package taskTrecker;

import taskTrecker.Tasks.*;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task task = new Task();
        task.setName("Сходить в магазин");
        task.setDescription("Нужно купить продукты на новый год");
        manager.doTask(task);

        Task task2 = new Task();
        task2.setName("Выбросить мусор");
        task2.setDescription("Нужно по дороге на работу взять и выбросить мусор");
        manager.doTask(task2);

        Epic epic1 = new Epic();
        epic1.setName("Переезд");
        epic1.setDescription("Нужно упаковать все вещи и попрощаться с соседями");
        manager.doEpicTask(epic1);

        SubTask subTask1 = new SubTask();
        subTask1.setName("Упаковать вещи");
        subTask1.setDescription("Собрать вещи в коробки");
        subTask1.setEpicObject(manager.getEpicTask(3));
        manager.doSubTask(subTask1);

        SubTask subTask2 = new SubTask();
        subTask2.setName("Попрощаться с соседями");
        subTask2.setDescription("Попрощатся с Олегом");
        subTask2.setEpicObject(manager.getEpicTask(3));
        manager.doSubTask(subTask2);

        Epic epic2 = new Epic();
        epic2.setName("Ремонт");
        epic2.setDescription("Нужно закончить ремонт в квартире");
        manager.doEpicTask(epic2);

        SubTask subTask3 = new SubTask();
        subTask3.setName("Купить инструменты");
        subTask3.setDescription("Сходить в магазин за молотком");
        subTask3.setEpicObject(manager.getEpicTask(6));
        manager.doSubTask(subTask3);

        System.out.println(manager);


        Task updateTask2 = manager.getTask(1);
        updateTask2.setStatus(StatusOfTask.DONE);
        updateTask2.setId(22);
        manager.updateTask(updateTask2);

        SubTask updateSub1 = manager.getSubTask(4);
        updateSub1.setStatus(StatusOfTask.DONE);
        manager.updateSubTask(updateSub1);

        SubTask updateSub3 = manager.getSubTask(7);
        if (updateSub3 != null) {
            updateSub3.setStatus(StatusOfTask.DONE);
            manager.updateSubTask(updateSub3);
        }


        System.out.println(manager);


        manager.removeTask(1);
        manager.removeEpicTask(3);
        System.out.println(manager);


    }
}
