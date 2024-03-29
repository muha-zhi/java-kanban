package taskTrecker.tasks.manager;

import taskTrecker.history.HistoryManager;
import taskTrecker.tasks.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {


    File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public FileBackedTasksManager() {
        super();
    }


    public void save() throws IOException, InterruptedException {

        try (FileWriter writer = new FileWriter(file, false)) {
            if (!epicTasks.isEmpty()) {
                for (Integer n : epicTasks.keySet()) {
                    String task = taskToString(epicTasks.get(n));
                    writer.append(task);
                    writer.write("\n");
                }
            }
            if (!tasks.isEmpty()) {
                for (Integer n : tasks.keySet()) {
                    String task = taskToString(tasks.get(n));
                    writer.append(task);
                    writer.write("\n");
                }
            }
            if (!subTasks.isEmpty()) {
                for (Integer n : subTasks.keySet()) {
                    String task = taskToString(subTasks.get(n));
                    writer.append(task);
                    writer.write("\n");
                }
            }
            writer.write("");
            writer.write("\n");
            writer.append(historyToString(historyManager));


        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }


    }

    public static List<Integer> historyFromString(String value) {

        List<Integer> idToInt = new ArrayList<>();

        String[] strOfId = value.split(",");
        for (String s : strOfId) {
            int id = Integer.parseInt(s);
            idToInt.add(id);
        }

        return idToInt;

    }


    public static String historyToString(HistoryManager manager) {

        List<Task> history = manager.getHistory();
        String[] idOfHistory = new String[history.size()];

        int val = 0;

        for (Task t : history) {

            idOfHistory[val] = Integer.toString(t.getId());
            val++;
        }
        return String.join(",", idOfHistory);
    }

    public static Task fromString(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy| HH:mm");

        String[] taskElements = value.split(",");

        TypeOfTask type = TypeOfTask.valueOf(taskElements[1]);
        Task taskForReturn = null;


        switch (type) {

            case TASK:
                taskForReturn = new Task(Integer.parseInt(taskElements[0]));
                taskForReturn.setName(taskElements[2]);
                taskForReturn.setDescription(taskElements[3]);
                taskForReturn.setStatus(StatusOfTask.valueOf(taskElements[4]));
                if (taskElements.length > 5) {
                    taskForReturn.setStartTime(LocalDateTime.parse(taskElements[5], formatter));
                    taskForReturn.setDuration(Duration.ofMinutes(Long.parseLong(taskElements[6])));
                }

            case EPIC:
                taskForReturn = new Epic(Integer.parseInt(taskElements[0]));
                taskForReturn.setName(taskElements[2]);
                taskForReturn.setDescription(taskElements[3]);
                taskForReturn.setStatus(StatusOfTask.valueOf(taskElements[4]));
                if (taskElements.length > 5) {
                    taskForReturn.setStartTime(LocalDateTime.parse(taskElements[5], formatter));
                    taskForReturn.setDuration(Duration.ofMinutes(Long.parseLong(taskElements[6])));
                }
                break;

            case SUBTASK:

                taskForReturn = new SubTask(Integer.parseInt(taskElements[0]));
                taskForReturn.setName(taskElements[2]);
                taskForReturn.setDescription(taskElements[3]);
                taskForReturn.setStatus(StatusOfTask.valueOf(taskElements[4]));
                ((SubTask) taskForReturn).setEpicObject(Integer.parseInt(taskElements[5]));
                if (taskElements.length > 6) {
                    taskForReturn.setStartTime(LocalDateTime.parse(taskElements[6], formatter));
                    taskForReturn.setDuration(Duration.ofMinutes(Long.parseLong(taskElements[7])));
                }
                break;

            default:
                break;
        }
        return taskForReturn;
    }


    public static String taskToString(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy| HH:mm");
        String id = Integer.toString(task.getId());
        String name = task.getName();
        String dis = task.getDescription();
        String status = task.getStatus().name();
        String idOfEpicForSub;
        LocalDateTime startTime = task.getStartTime();
        Duration duration = task.getDuration();
        String typeOfTask = "TASK";

        String forReturn = id + "," + typeOfTask + "," + name + "," + dis + "," + status;
        if (startTime != null) {
            forReturn += "," + startTime.format(formatter)
                    + "," + duration.toMinutes();
        }
        if (task.getClass() == Epic.class) {
            typeOfTask = "EPIC";
            forReturn = id + "," + typeOfTask + "," + name + "," + dis + "," + status;
            if (startTime != null) {
                forReturn += "," + startTime.format(formatter)
                        + "," + duration.toMinutes();
            }
        } else if (task.getClass() == SubTask.class) {
            typeOfTask = "SUBTASK";
            idOfEpicForSub = Integer.toString(((SubTask) task).getEpicObject());
            forReturn = id + "," + typeOfTask + "," + name + "," + dis + "," + status + "," + idOfEpicForSub;
            if (startTime != null) {
                forReturn += "," + startTime.format(formatter)
                        + "," + duration.toMinutes();
            }
        }
        return forReturn;
    }

    @Override
    public void doTask(Task task) throws IOException {
        try {
            super.doTask(task);
            save();
        } catch (InterruptedException e) {
            throw new ManagerSaveException(e);
        }

    }

    @Override
    public void doEpicTask(Epic epic) throws IOException {
        try {
            super.doEpicTask(epic);
            save();
        } catch (InterruptedException e) {
            throw new ManagerSaveException(e);
        }


    }

    @Override
    public void doSubTask(SubTask sub) throws IOException {
        try {
            super.doSubTask(sub);
            save();
        } catch (InterruptedException e) {
            throw new ManagerSaveException(e);
        }


    }

    @Override
    public void updateTask(Task task) throws IOException {
        try {
            super.updateTask(task);
            save();
        } catch (InterruptedException e) {
            throw new ManagerSaveException(e);
        }

    }

    @Override
    public void updateEpicTask(Epic epic) throws IOException {
        try {
            super.updateEpicTask(epic);
            save();
        } catch (InterruptedException e) {
            throw new ManagerSaveException(e);
        }

    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException {

        try {
            super.updateSubTask(subTask);
            save();
        } catch (InterruptedException e) {
            throw new ManagerSaveException(e);
        }

    }

    @Override
    public void clearTasks() throws IOException {

        try {
            super.clearTasks();
            save();
        } catch (InterruptedException e) {
            throw new ManagerSaveException(e);
        }

    }

    @Override
    public void clearEpicTasks() throws IOException {
        try {
            super.clearEpicTasks();
            save();
        } catch (InterruptedException e) {
            throw new ManagerSaveException(e);
        }
    }

    @Override
    public void clearSubTasks() throws IOException {

        try {
            super.clearSubTasks();
            save();
        } catch (InterruptedException e) {
            throw new ManagerSaveException(e);
        }
    }

    @Override
    public void removeTask(int id) throws IOException {
        super.removeTask(id);
        try {
            save();
        } catch (InterruptedException e) {
            throw new ManagerSaveException(e);
        }
    }

    @Override
    public void removeSubTask(int id) throws IOException, InterruptedException {
        super.removeSubTask(id);
        try {
            save();
        } catch (InterruptedException e) {
            throw new ManagerSaveException(e);
        }
    }

    @Override
    public void removeEpicTask(int id) throws IOException {

        super.removeEpicTask(id);
        try {
            save();
        } catch (InterruptedException e) {
            throw new ManagerSaveException(e);
        }


    }

    @Override
    public SubTask getSubTask(int id) throws ManagerSaveException {
        SubTask subTask = super.getSubTask(id);
        try {

            save();
            return subTask;
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e);
        }


    }

    @Override
    public Epic getEpicTask(int id) throws ManagerSaveException {
        Epic epic = super.getEpicTask(id);
        try {

            save();
            return epic;
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e);
        }


    }


    @Override
    public Task getTask(int id) throws ManagerSaveException {
        Task task = super.getTask(id);
        try {

            save();
            return task;
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e);
        }


    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {

        FileBackedTasksManager manager = new FileBackedTasksManager(file);

        int indexOfHistory = 0;
        List<String> taskLines = readFileContents(file);

        for (int i = 0; i < taskLines.size(); i++) {
            if (taskLines.get(i).equals("")) {
                if (!((i + 2) > taskLines.size())) {
                    indexOfHistory = i + 1;
                    break;
                }
            } else {

                String[] str = taskLines.get(i).split(",");
                TypeOfTask type = TypeOfTask.valueOf(str[1]);
                switch (type) {
                    case EPIC:
                        manager.doEpicTask((Epic) fromString(taskLines.get(i)));
                        break;

                    case SUBTASK:
                        manager.doSubTask((SubTask) fromString(taskLines.get(i)));
                        break;

                    case TASK:
                        manager.doTask(fromString(taskLines.get(i)));
                        break;

                    default:
                        break;
                }


            }


        }
        if (indexOfHistory != 0) {
            List<Integer> historyIdOfTasks = historyFromString(taskLines.get(indexOfHistory));
            for (Integer i : historyIdOfTasks) {
                if (manager.tasks.containsKey(i)) {
                    manager.historyManager.add(manager.tasks.get(i));
                } else if (manager.epicTasks.containsKey(i)) {
                    manager.historyManager.add(manager.epicTasks.get(i));
                } else if (manager.subTasks.containsKey(i)) {
                    manager.historyManager.add(manager.subTasks.get(i));
                }
            }
        }


        for (Epic epic : manager.getListOfEpic()) {
            List<SubTask> subs = new ArrayList<>();
            for (SubTask sub : manager.getListOfSub()) {
                if (sub.getEpicObject() == epic.getId()) {
                    subs.add(sub);
                }
            }
            epic.setSubsOfThisEpic(subs);
        }
        return manager;
    }


    static List<String> readFileContents(File path) {
        try {
            return Files.readAllLines(Path.of(String.valueOf(path.toPath())));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл");
            return Collections.emptyList();
        }
    }

}
