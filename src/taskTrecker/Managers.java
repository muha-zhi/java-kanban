package taskTrecker;


import taskTrecker.tasks.InMemoryTaskManager;
import taskTrecker.tasks.TaskManager;
import taskTrecker.history.HistoryManager;
import taskTrecker.history.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {

        return new InMemoryHistoryManager();

    }
}
