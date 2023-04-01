package taskTrecker.tasks.manager;


import taskTrecker.history.HistoryManager;
import taskTrecker.history.InMemoryHistoryManager;

public class Managers {


    private static TaskManager managerOfTask;
    private static HistoryManager historyManager;

    public static TaskManager getDefault() {

        if (managerOfTask == null) {
            managerOfTask = new InMemoryTaskManager();
        }


        return managerOfTask;
    }

    public static HistoryManager getDefaultHistory() {

        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }


        return historyManager;

    }


}
