package taskTrecker;


import taskTrecker.history.HistoryManager;
import taskTrecker.tasks.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Epic epic1 = new Epic();
        epic1.setName("Переезд");
        epic1.setDescription("Нужно упаковать все вещи и попрощаться с соседями");
        manager.doEpicTask(epic1);

        Epic epic2 = new Epic();
        epic2.setName("Ремонт");
        epic2.setDescription("Нужно закончить ремонт в квартире");
        manager.doEpicTask(epic2);

        SubTask subTask1 = new SubTask();
        subTask1.setName("Упаковать вещи");
        subTask1.setDescription("Собрать вещи в коробки");
        subTask1.setEpicObject(manager.getEpicTask(1));
        manager.doSubTask(subTask1);

        SubTask subTask2 = new SubTask();
        subTask2.setName("Попрощаться с соседями");
        subTask2.setDescription("Попрощатся с Олегом");
        subTask2.setEpicObject(manager.getEpicTask(1));
        manager.doSubTask(subTask2);

        SubTask subTask3 = new SubTask();
        subTask3.setName("Купить инструменты");
        subTask3.setDescription("Сходить в магазин за молотком");
        subTask3.setEpicObject(manager.getEpicTask(1));
        manager.doSubTask(subTask3);

        manager.getEpicTask(1);
        manager.getEpicTask(2);
        manager.getSubTask(3);
        manager.getSubTask(5);
        manager.getSubTask(4);



        manager.getEpicTask(1);
        manager.getSubTask(5);
        manager.getSubTask(4);
        manager.getSubTask(3);



        manager.getSubTask(3);
        manager.getSubTask(4);
        manager.getSubTask(5);
        manager.getEpicTask(1);
        System.out.println(manager);

        List<Task> test = historyManager.getHistory();
        for(Task task : test){
            System.out.println(task);

        }


    }
}
