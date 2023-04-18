package taskTrecker.tasks.manager;


import taskTrecker.tasks.Epic;
import taskTrecker.tasks.SubTask;
import taskTrecker.tasks.Task;

import java.io.IOException;
import java.util.List;


public interface TaskManager {


    public int getIdOfAll();

    public void setIdOfAll(int idOfAll);

    public List<Task> getPrioritizedTasks();


    public void doSort();


    public void doTask(Task task) throws IOException, InterruptedException;


    public void doEpicTask(Epic epic) throws IOException, InterruptedException;


    public void doSubTask(SubTask sub) throws IOException, InterruptedException;


    public List<Epic> getListOfEpic() throws NullPointerException;


    public List<Task> getListOfTask() throws NullPointerException;


    public List<SubTask> getListOfSub() throws NullPointerException;


    public void updateTask(Task task) throws IOException, InterruptedException;


    public void updateEpicTask(Epic epic) throws IOException, InterruptedException;


    public void updateSubTask(SubTask subTask) throws IOException, InterruptedException;


    public SubTask getSubTask(int id) throws ManagerSaveException, NullPointerException;


    public Epic getEpicTask(int id) throws ManagerSaveException, NullPointerException;

    public Task getTask(int id) throws ManagerSaveException, NullPointerException;


    public void clearTasks() throws IOException, InterruptedException;

    public void clearEpicTasks() throws IOException, InterruptedException;


    public void clearSubTasks() throws IOException;


    public void removeTask(int id) throws IOException;

    public void removeSubTask(int id) throws IOException, InterruptedException;


    public void removeEpicTask(int id) throws IOException;


    public List<SubTask> getSubsOfEpic(int id);

    public boolean isIntersects(Task task);


}




