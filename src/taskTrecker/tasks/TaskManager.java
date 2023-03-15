package taskTrecker.tasks;


import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface TaskManager {


    public int getIdOfAll();


    public Map<Integer, Task> getTasks();


    public Map<Integer, SubTask> getSubTasks();


    public Map<Integer, Epic> getEpicTasks();


    public void doTask(Task task) throws IOException;


    public void doEpicTask(Epic epic) throws IOException;


    public void doSubTask(SubTask sub)throws IOException;


    public List<Epic> getListOfEpic();


    public List<Task> getListOfTask();


    public List<SubTask> getListOfSub();


    public void updateTask(Task task)throws IOException;


    public void updateEpicTask(int id)throws IOException;


    public void updateSubTask(SubTask subTask)throws IOException;


    public SubTask getSubTask(int id);


    public int getEpicTask(int id);

    public Task getTask(int id);


    public void clearTasks()throws IOException;

    public void clearEpicTasks()throws IOException;


    public void clearSubTasks()throws IOException;


    public void removeTask(int id)throws IOException;

    public void removeSubTask(int id)throws IOException;


    public void removeEpicTask(int id)throws IOException;


    public List<SubTask> getSubsOfEpic(int id);


}




