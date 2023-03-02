package taskTrecker.tasks;



import taskTrecker.history.HistoryManager;
import taskTrecker.history.InMemoryHistoryManager;

public class Managers {
   static   TaskManager managerOfTask;
   static HistoryManager historyManager;
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
