package taskTrecker.tasks.manager;

import taskTrecker.history.HistoryManager;
import taskTrecker.tasks.Epic;
import taskTrecker.tasks.StatusOfTask;
import taskTrecker.tasks.SubTask;
import taskTrecker.tasks.Task;

import java.io.IOException;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int idOfAll = 0;
    public HistoryManager historyManager = Managers.getDefaultHistory();


    public int getIdOfAll() {
        idOfAll += 1;
        return idOfAll;
    }

    public void setIdOfAll(int idOfAll) {
        this.idOfAll = idOfAll;
    }


    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epicTasks = new HashMap<>();
    protected Set<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected List<Task> taskWithoutDate = new ArrayList<>();

    @Override
    public boolean isIntersects(Task task) {
        boolean forReturn = false;
        for (Task t : sortedTasks) {
            if (task.getStartTime().isBefore(t.getStartTime()) && task.getEndTime().isAfter(t.getEndTime())
                    || task.getEndTime().isAfter(t.getStartTime()) && task.getEndTime().isBefore(t.getEndTime())
                    || task.getStartTime().isBefore(t.getStartTime()) && task.getEndTime().isAfter(t.getEndTime())) {
                forReturn = true;
            }
        }
        return forReturn;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        doSort();
        List<Task> forReturn = new ArrayList<>();
        forReturn.addAll(sortedTasks);
        forReturn.addAll(taskWithoutDate);
        return forReturn;

    }

    @Override
    public void doSort() {
        for (Integer k : epicTasks.keySet()) {
            if (k != null) {
                if (epicTasks.get(k).getStartTime() == null) {
                    taskWithoutDate.add(epicTasks.get(k));
                } else {
                    sortedTasks.add(epicTasks.get(k));
                }
            }
        }
        for (Integer k : subTasks.keySet()) {
            if (k != null) {
                if (subTasks.get(k).getStartTime() == null) {
                    taskWithoutDate.add(subTasks.get(k));
                } else {
                    sortedTasks.add(subTasks.get(k));
                }
            }
        }
        for (Integer k : tasks.keySet()) {
            if (k != null) {
                if (tasks.get(k).getStartTime() == null) {
                    taskWithoutDate.add(tasks.get(k));
                } else {
                    sortedTasks.add(tasks.get(k));
                }
            }
        }
    }


    @Override
    public void doTask(Task task) throws IOException {
        doSort();
        if (isIntersects(task)) {
            System.out.println("Задача пересекается с другими задачами");
        } else {


            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void doEpicTask(Epic epic) throws IOException {
        doSort();
        if (isIntersects(epic)) {
            System.out.println("Задача пересекается с другими задачами");
        } else {
            if (epic != null) {
                epic.setStartTime();
                epic.setEndTime();
            }

            if (epic != null && epic.getId() != 0) {
                epicTasks.put(epic.getId(), epic);

            }
        }


    }

    @Override
    public void doSubTask(SubTask sub) throws IOException {
        doSort();
        if (isIntersects(sub)) {
            System.out.println("Задача пересекается с другими задачами");
        } else {
            if (sub != null) {

                subTasks.put(sub.getId(), sub);
                Epic epic = epicTasks.get(sub.getEpicObject());
                if (epic != null) {
                    epic.addSubForEpic(sub);
                }

                updateEpicTask(epicTasks.get(sub.getEpicObject()));

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
        List<Task> tasks1 = new ArrayList<>();

        for (Integer key : tasks.keySet()) {
            tasks1.add(tasks.get(key));

        }

        return tasks1;

    }

    @Override

    public List<SubTask> getListOfSub() {
        List<SubTask> subTasks1 = new ArrayList<>();
        for (Integer key : subTasks.keySet()) {
            subTasks1.add(subTasks.get(key));

        }
        return subTasks1;
    }

    @Override
    public void updateTask(Task task) throws IOException, IllegalStateException {
        doSort();
        if (isIntersects(task)) {
            System.out.println("Задача пересекается с другими задачами");
        } else {
            if (!tasks.containsKey(task.getId())) {
                throw new IllegalStateException("Задача не найдена.");
            } else {
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
        }
    }

    @Override
    public void updateEpicTask(Epic epic) throws IOException {
        doSort();
        if (isIntersects(epic)) {
            System.out.println("Задача пересекается с другими задачами");
        } else {

            if (epic != null) {
                if (!epicTasks.containsKey(epic.getId())) {
                    throw new IllegalStateException("Задача не найдена.");
                } else {
                    epic.setStartTime();
                    epic.setEndTime();

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
        }

    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException {
        doSort();
        if (isIntersects(subTask)) {
            System.out.println("Задача пересекается с другими задачами");
        } else {
            if (subTask != null) {
                if (!subTasks.containsKey(subTask.getId())) {
                    throw new IllegalStateException("Задача не найдена.");
                } else {
                    subTasks.put(subTask.getId(), subTask);
                    updateEpicTask(epicTasks.get(subTask.getEpicObject()));
                }
            }
        }
    }

    @Override
    public SubTask getSubTask(int id) throws ManagerSaveException {
        if (!subTasks.isEmpty()) {
            if (!subTasks.containsKey(id)) {
                throw new IllegalStateException("Задача не найдена.");
            } else {
                if (historyManager != null) {
                    historyManager.add(subTasks.get(id));

                }
                return subTasks.get(id);
            }

        }
        return null;
    }

    @Override
    public Epic getEpicTask(int id) throws ManagerSaveException {

        if (!epicTasks.isEmpty()) {
            if (!epicTasks.containsKey(id)) {
                throw new IllegalStateException("Задача не найдена.");
            } else {
                Task epic = epicTasks.get(id);
                if (historyManager != null) {

                    historyManager.add(epic);
                }

                return epicTasks.get(id);
            }
        }
        return null;
    }

    @Override
    public Task getTask(int id) throws ManagerSaveException {
        if (!tasks.isEmpty()) {
            if (!tasks.containsKey(id)) {
                throw new IllegalStateException("Задача не найдена.");
            } else {
                historyManager.add(tasks.get(id));
                return tasks.get(id);
            }
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
        if (!tasks.containsKey(id)) {
            throw new IllegalStateException("Задача не найдена.");
        } else {
            Task task = tasks.get(id);
            if (task != null) {
                historyManager.remove(task);
                tasks.remove(id);
            }
        }
    }

    @Override
    public void removeSubTask(int id) throws IOException {
        if (!subTasks.containsKey(id)) {
            throw new IllegalStateException("Задача не найдена.");
        } else {
            historyManager.remove(subTasks.get(id));
            subTasks.remove(id);
            SubTask subTask = subTasks.get(id);
            if (subTask != null) {
                updateEpicTask(epicTasks.get(subTask.getEpicObject()));
            }
        }
    }

    @Override
    public void removeEpicTask(int id) throws IOException {
        if (!epicTasks.containsKey(id)) {
            throw new IllegalStateException("Задача не найдена.");
        } else {

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
