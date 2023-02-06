package taskTrecker;

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


    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epicTasks = new HashMap<>();


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
    public void doTask(Task task) {
        task.setId(getIdOfAll());
        tasks.put(task.getId(), task);
    }

    @Override
    public void doEpicTask(Epic epic) {
        epic.setId(getIdOfAll());
        epicTasks.put(epic.getId(), epic);
    }

    @Override
    public void doSubTask(SubTask sub) {
        sub.setId(getIdOfAll());
        for (Integer key : epicTasks.keySet()) {
            if (epicTasks.get(key).equals(sub.getEpicObject())) {
                sub.setEpicObject(epicTasks.get(key));
                updateEpicTask(sub.getId());
                subTasks.put(sub.getId(), sub);
                epicTasks.get(key).addSubTasks(sub);
            }
        }


    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getListOfEpic() {
        List<Epic> epics = new ArrayList<>();
        for (Integer key : epicTasks.keySet()) {
            epics.add(epicTasks.get(key));
            System.out.println(epicTasks.get(key));
        }
        return epics;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getListOfTask() {
        List<Task> taskss = new ArrayList<>();

        for (Integer key : tasks.keySet()) {
            taskss.add(epicTasks.get(key));

        }
        return taskss;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getListOfSub() {
        List<SubTask> subTaskss = new ArrayList<>();
        for (Integer key : subTasks.keySet()) {
            subTaskss.add(subTasks.get(key));

        }
        return subTaskss;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpicTask(int id) {
        SubTask subTask = (subTasks.get(id));
        if (subTask != null) {
            Epic epic = subTask.getEpicObject();
            StatusOfTask statusOfEpic;
            boolean doneTrue = false;
            boolean newTrue = false;
            for (SubTask sub : epic.getSubTasks()) {
                if (sub.status != null) {
                    if (sub.status == StatusOfTask.NEW) {
                        newTrue = true;
                    } else if (sub.status == StatusOfTask.DONE) {
                        doneTrue = true;
                    }
                }
            }
            if (doneTrue && !newTrue) {
                statusOfEpic = StatusOfTask.DONE;
            } else if (!doneTrue && newTrue || epic.getSubTasks().isEmpty()) {
                statusOfEpic = StatusOfTask.NEW;
            } else {
                statusOfEpic = StatusOfTask.IN_PROGRESS;
            }
            epic.status = statusOfEpic;
            epicTasks.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {

        subTasks.put(subTask.getId(), subTask);
        updateEpicTask(subTask.getId());
    }

    @Override
    public SubTask getSubTask(int id) {
        if (!subTasks.isEmpty()) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpicTask(int id) {
        if (!epicTasks.isEmpty()) {
            historyManager.add(epicTasks.get(id));
            return epicTasks.get(id);
        }
        return null;
    }

    @Override
    public Task getTask(int id) {
        if (!tasks.isEmpty()) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpicTasks() {
        epicTasks.clear();
    }

    @Override
    public void clearSubTasks() {
        subTasks.clear();
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeSubTask(int id) {
        subTasks.remove(id);
        SubTask subTask = subTasks.get(id);
        updateEpicTask(subTask.getId());
    }

    @Override
    public void removeEpicTask(int id) {

        ArrayList<Integer> keys = new ArrayList<>();

        for (Integer key : subTasks.keySet()) {
            SubTask sub = subTasks.get(key);
            if (sub.getEpicObject().equals(epicTasks.get(id))) {
                keys.add(key);
            }
        }
        for (Integer key : keys) {
            subTasks.remove(key);
        }
        epicTasks.remove(id);


    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getSubsOfEpic(int id) {
        Epic epic = epicTasks.get(id);
        return epic.getSubTasks();
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
