package taskTrecker.tasks.manager;


import taskTrecker.history.InMemoryHistoryManager;

import java.io.File;
import java.io.IOException;

public class Managers {


    private static TaskManager managerOfTask;
    private static InMemoryHistoryManager historyManager;

    private static HttpTaskManager HttpManager;

    private static FileBackedTasksManager fileBackedTasksManager;

    public static TaskManager getDefault() {

        if (managerOfTask == null) {
            managerOfTask = new InMemoryTaskManager();
        }


        return managerOfTask;
    }

    public static InMemoryHistoryManager getDefaultHistory() {

        if (historyManager == null) {
            historyManager = new InMemoryHistoryManager();
        }


        return historyManager;

    }

    public static HttpTaskManager getHttpTasksManager() throws IOException, InterruptedException {
        if (HttpManager == null) {
            HttpManager = new HttpTaskManager();
        }
        return HttpManager;
    }

    public static FileBackedTasksManager getFileManager() {
        if (fileBackedTasksManager == null) {
            fileBackedTasksManager = new FileBackedTasksManager(new File("C:\\Users" +
                    "\\mzile\\dev\\java-kanban\\src\\taskTrecker\\resources\\res.csv").getAbsoluteFile());
        }
        return fileBackedTasksManager;
    }


}
