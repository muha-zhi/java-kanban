package taskTrecker.tasks.Manager;

import taskTrecker.history.HistoryManager;
import taskTrecker.tasks.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {


    File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }


    public void save() throws ManagerSaveException {

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

        String[] taskElements = value.split(",");

        TypeOfTask type = TypeOfTask.valueOf(taskElements[1]);
        Task taskForReturn = new Task(Integer.parseInt(taskElements[0]));
        switch (type) {
            case EPIC:
                taskForReturn = new Epic(Integer.parseInt(taskElements[0]));
                taskForReturn.setName(taskElements[2]);
                taskForReturn.setDescription(taskElements[3]);
                taskForReturn.setStatus(StatusOfTask.valueOf(taskElements[4]));
                break;

            case SUBTASK:
                taskForReturn = new SubTask(Integer.parseInt(taskElements[0]));
                taskForReturn.setName(taskElements[2]);
                taskForReturn.setDescription(taskElements[3]);
                taskForReturn.setStatus(StatusOfTask.valueOf(taskElements[4]));
                ((SubTask) taskForReturn).setEpicObject(Integer.parseInt(taskElements[5]));
                break;

            case TASK:


                taskForReturn.setName(taskElements[2]);
                taskForReturn.setDescription(taskElements[3]);
                taskForReturn.setStatus(StatusOfTask.valueOf(taskElements[4]));
                break;

            default:
                break;
        }
        return taskForReturn;
    }


    public static String taskToString(Task task) {
        String id = Integer.toString(task.getId());
        String name = task.getName();
        String dis = task.getDescription();
        String status = task.getStatus().name();
        String idOfEpicForSub;
        String typeOfTask = "TASK";

        String forReturn = id + "," + typeOfTask + "," + name + "," + dis + "," + status;
        if (task.getClass() == Epic.class) {
            typeOfTask = "EPIC";
            forReturn = id + "," + typeOfTask + "," + name + "," + dis + "," + status;
        } else if (task.getClass() == SubTask.class) {
            typeOfTask = "SUBTASK";
            idOfEpicForSub = Integer.toString(((SubTask) task).getEpicObject());
            forReturn = id + "," + typeOfTask + "," + name + "," + dis + "," + status + "," + idOfEpicForSub;
        }
        return forReturn;
    }

    @Override
    public void doTask(Task task) throws IOException {
        super.doTask(task);
        save();
    }

    @Override
    public void doEpicTask(Epic epic) throws IOException {
        super.doEpicTask(epic);
        save();

    }

    @Override
    public void doSubTask(SubTask sub) throws IOException {
        super.doSubTask(sub);
        save();


    }

    @Override
    public void updateTask(Task task) throws IOException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicTask(int id) throws IOException {
        super.updateEpicTask(id);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException {

        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void clearTasks() throws IOException {

        super.clearTasks();
        save();
    }

    @Override
    public void clearEpicTasks() throws IOException {
        super.clearEpicTasks();
        save();
    }

    @Override
    public void clearSubTasks() throws IOException {
        super.clearSubTasks();
        save();
    }

    @Override
    public void removeTask(int id) throws IOException {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubTask(int id) throws IOException {
        super.removeSubTask(id);
        save();
    }

    @Override
    public void removeEpicTask(int id) throws IOException {

        super.removeEpicTask(id);
        save();


    }

    @Override
    public SubTask getSubTask(int id) throws ManagerSaveException {

        save();

        return super.getSubTask(id);

    }

    @Override
    public int getEpicTask(int id) throws ManagerSaveException {


        save();

        return super.getEpicTask(id);

    }

    @Override
    public Task getTask(int id) throws ManagerSaveException {

        save();

        return super.getTask(id);
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {

        FileBackedTasksManager manager = new FileBackedTasksManager(file);

        int indexOfHistory = 0;
        List<String> taskLines = readFileContents(file.getName());

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

    public static void main(String[] args) throws IOException {



        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(new File("C:\\Users\\mzile\\dev\\java-kanban\\src\\taskTrecker\\resources\\res.csv").getAbsoluteFile());

        Task task = new Task();
        task.setName("ighogui");
        task.setDescription("gugvouhgou");
        fileBackedTasksManager1.doTask(task);
        Task task2 = new Task();
        task2.setName("oooooooooo");
        task2.setDescription("gugvouhgou");
        fileBackedTasksManager1.doTask(task2);
        fileBackedTasksManager1.getTask(1);
        fileBackedTasksManager1.getTask(2);


        System.out.println(fileBackedTasksManager1);
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("src\\taskTrecker\\resources\\res.csv").getAbsoluteFile());
        System.out.println(fileBackedTasksManager);

    }

    @Override
    public String toString() {
        return super.toString();
    }

    static List<String> readFileContents(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл с месячным отчётом. Возможно файл не находится в нужной директории.");
            return Collections.emptyList();
        }
    }

}
