package taskTrecker.history;

import taskTrecker.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(Task task);

    void clearHistory();


    List<Task> getHistory();

}
