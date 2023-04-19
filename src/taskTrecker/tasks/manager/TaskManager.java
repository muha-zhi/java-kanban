package taskTrecker.tasks.manager;


import taskTrecker.tasks.Epic;
import taskTrecker.tasks.SubTask;
import taskTrecker.tasks.Task;

import java.io.IOException;
import java.util.List;


public interface TaskManager {


     int getIdOfAll();

     void setIdOfAll(int idOfAll);

     List<Task> getPrioritizedTasks();


     void doSort();


     void doTask(Task task) throws IOException, InterruptedException;


     void doEpicTask(Epic epic) throws IOException, InterruptedException;


     void doSubTask(SubTask sub) throws IOException, InterruptedException;


     List<Epic> getListOfEpic() throws NullPointerException;


     List<Task> getListOfTask() throws NullPointerException;


     List<SubTask> getListOfSub() throws NullPointerException;


     void updateTask(Task task) throws IOException, InterruptedException;


     void updateEpicTask(Epic epic) throws IOException, InterruptedException;


     void updateSubTask(SubTask subTask) throws IOException, InterruptedException;


     SubTask getSubTask(int id) throws ManagerSaveException, NullPointerException;


     Epic getEpicTask(int id) throws ManagerSaveException, NullPointerException;

     Task getTask(int id) throws ManagerSaveException, NullPointerException;


     void clearTasks() throws IOException, InterruptedException;

     void clearEpicTasks() throws IOException, InterruptedException;


     void clearSubTasks() throws IOException;


     void removeTask(int id) throws IOException;

     void removeSubTask(int id) throws IOException, InterruptedException;


     void removeEpicTask(int id) throws IOException;


     List<SubTask> getSubsOfEpic(int id);

     boolean isIntersects(Task task);


}




