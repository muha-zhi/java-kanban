package taskTrecker.history;

import taskTrecker.Tasks.Task;
import taskTrecker.history.HistoryManager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    public List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > 10) {
            history.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
