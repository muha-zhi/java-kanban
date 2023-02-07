package taskTrecker.Tasks;


import taskTrecker.Tasks.Epic;
import taskTrecker.Tasks.SubTask;
import taskTrecker.Tasks.Task;

import java.util.List;
import java.util.Map;


public interface TaskManager {


    public int getIdOfAll();


    public Map<Integer, Task> getTasks();


    public Map<Integer, SubTask> getSubTasks();


    public Map<Integer, Epic> getEpicTasks();


    public void doTask(Task task);


    public void doEpicTask(Epic epic);


    public void doSubTask(SubTask sub);


    public List getListOfEpic();


    public List getListOfTask();


    public List getListOfSub();


    public void updateTask(Task task);


    public void updateEpicTask(int id);


    public void updateSubTask(SubTask subTask);


    public SubTask getSubTask(int id);


    public Epic getEpicTask(int id);

    public Task getTask(int id);


    public void clearTasks();

    public void clearEpicTasks();


    public void clearSubTasks();


    public void removeTask(int id);

    public void removeSubTask(int id);


    public void removeEpicTask(int id);


    public List getSubsOfEpic(int id);


}




