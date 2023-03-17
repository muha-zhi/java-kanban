package taskTrecker.tasks.Manager;

import taskTrecker.history.HistoryManager;
import taskTrecker.tasks.Epic;
import taskTrecker.tasks.StatusOfTask;
import taskTrecker.tasks.SubTask;
import taskTrecker.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int idOfAll = 0;
    public HistoryManager historyManager = Managers.getDefaultHistory();


    public int getIdOfAll() {
        idOfAll += 1;
        return idOfAll;
    }


    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epicTasks = new HashMap<>();


    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }


    @Override
    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public Map<Integer, Epic> getEpicTasks() {
        return epicTasks;
    }

    @Override
    public void doTask(Task task) throws IOException {


        tasks.put(task.getId(), task);
    }

    @Override
    public void doEpicTask(Epic epic) throws IOException {

        epicTasks.put(epic.getId(), epic);
    }

    @Override
    public void doSubTask(SubTask sub) throws IOException {

        for (Integer key : epicTasks.keySet()) {
            if (epicTasks.get(key).getId() == sub.getEpicObject()) {
                sub.setEpicObject(epicTasks.get(key).getId());
                updateEpicTask(sub.getId());
                subTasks.put(sub.getId(), sub);

            }
        }


    }

    @Override

    public List<Epic> getListOfEpic() {
        List<Epic> epics = new ArrayList<>();
        for (Integer key : epicTasks.keySet()) {
            epics.add(epicTasks.get(key));
            System.out.println(epicTasks.get(key));
        }
        return epics;
    }

    @Override

    public List<Task> getListOfTask() {
        List<Task> taskss = new ArrayList<>();

        for (Integer key : tasks.keySet()) {
            taskss.add(epicTasks.get(key));

        }
        return taskss;
    }

    @Override

    public List<SubTask> getListOfSub() {
        List<SubTask> subTaskss = new ArrayList<>();
        for (Integer key : subTasks.keySet()) {
            subTaskss.add(subTasks.get(key));

        }
        return subTaskss;
    }

    @Override
    public void updateTask(Task task) throws IOException {
        int keyOfTask = 0;
        for (Integer key : tasks.keySet()) {
            Task task1 = tasks.get(key);
            if (task1 != null) {
                if (task.equals(task1)) {
                    keyOfTask = key;
                }
            }
        }
        tasks.remove(keyOfTask);

        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpicTask(int id) throws IOException {
        SubTask subTask = (subTasks.get(id));
        if (subTask != null) {
            Epic epic = epicTasks.get(subTask.getEpicObject());
            StatusOfTask statusOfEpic;
            boolean doneTrue = false;
            boolean newTrue = false;
            for (SubTask sub : getSubsOfEpic(epic.getId())) {
                if (sub.getStatus() != null) {
                    if (sub.getStatus() == StatusOfTask.NEW) {
                        newTrue = true;
                    } else if (sub.getStatus() == StatusOfTask.DONE) {
                        doneTrue = true;
                    }
                }
            }
            if (doneTrue && !newTrue) {
                statusOfEpic = StatusOfTask.DONE;
            } else if (!doneTrue && newTrue || getSubsOfEpic(epic.getId()).isEmpty()) {
                statusOfEpic = StatusOfTask.NEW;
            } else {
                statusOfEpic = StatusOfTask.IN_PROGRESS;
            }
            epic.setStatus(statusOfEpic);
            epicTasks.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException {

        subTasks.put(subTask.getId(), subTask);
        updateEpicTask(subTask.getId());
    }

    @Override
    public SubTask getSubTask(int id) throws ManagerSaveException {
        if (!subTasks.isEmpty()) {
            if (historyManager != null) {
                historyManager.add(subTasks.get(id));

            }
            return subTasks.get(id);
        }
        return null;
    }

    @Override
    public int getEpicTask(int id) throws ManagerSaveException {

        if (!epicTasks.isEmpty()) {
            Task epic = epicTasks.get(id);
            if (historyManager != null) {

                historyManager.add(epic);
            }

            return epicTasks.get(id).getId();
        }
        return 0;
    }

    @Override
    public Task getTask(int id) throws ManagerSaveException {
        if (!tasks.isEmpty()) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public void clearTasks() throws IOException {
        tasks.clear();
    }

    @Override
    public void clearEpicTasks() throws IOException {
        epicTasks.clear();
    }

    @Override
    public void clearSubTasks() throws IOException {
        subTasks.clear();
    }

    @Override
    public void removeTask(int id) throws IOException {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.remove(task);
            tasks.remove(id);
        }
    }

    @Override
    public void removeSubTask(int id) throws IOException {
        historyManager.remove(subTasks.get(id));
        subTasks.remove(id);
        SubTask subTask = subTasks.get(id);
        updateEpicTask(subTask.getId());
    }

    @Override
    public void removeEpicTask(int id) throws IOException {

        ArrayList<Integer> keys = new ArrayList<>();

        for (Integer key : subTasks.keySet()) {
            SubTask sub = subTasks.get(key);
            if (sub.getEpicObject() == epicTasks.get(id).getId()) {
                keys.add(key);
            }
        }
        for (Integer key : keys) {
            historyManager.remove(subTasks.get(key));
            subTasks.remove(key);
        }
        historyManager.remove(epicTasks.get(id));
        epicTasks.remove(id);


    }

    @Override
    public List<SubTask> getSubsOfEpic(int id) {
        List<SubTask> subs = new ArrayList<>();

        for (Integer key : subTasks.keySet()) {
            SubTask sub = subTasks.get(key);
            if (sub.getEpicObject() == id) {
                subs.add(sub);
            }
        }
        return subs;

    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Manager{" + "idOfAll=" + idOfAll + "\n" + "Tasks=" + "\n");
        for (Integer key : tasks.keySet()) {
            result.append(tasks.get(key)).append("\n");
        }
        result.append("SubTask=" + "\n");
        for (Integer key : subTasks.keySet()) {
            result.append(subTasks.get(key)).append("\n");
        }
        result.append("EpicTasks=").append("\n");
        for (Integer key : epicTasks.keySet()) {
            result.append(epicTasks.get(key)).append("\n");
        }
        result.append("History=").append("\n");
        for (Task task : historyManager.getHistory()) {
            result.append(task).append("\n");
        }


        return result.toString();
    }
}
