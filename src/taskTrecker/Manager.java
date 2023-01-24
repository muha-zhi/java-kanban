package taskTrecker;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Manager {
    private int idOfAll = 0;


    public int getIdOfAll() {
        idOfAll += 1;
        return idOfAll;
    }



    HashMap<Integer, Task> tasks = new HashMap<>();
     HashMap<Integer, SubTask> subTasks = new HashMap<>();
     HashMap<Integer, Epic> epicTasks = new HashMap<>();

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, Epic> getEpicTasks() {
        return epicTasks;
    }


    public void doTask(Task task){
        task.status = "NEW";
        task.setId(getIdOfAll());
        tasks.put(task.getId(), task);
    }
    public void doEpicTask(Epic epic){
        epic.status = "NEW";
        epic.setId(getIdOfAll());
        epicTasks.put(epic.getId(), epic);
    }

    public void doSubTask(SubTask sub){
        sub.setId(getIdOfAll());
        for(Integer key : epicTasks.keySet()){
            if (epicTasks.get(key).equals(sub.getEpicObject())){
                sub.setEpicObject(epicTasks.get(key));
                updateEpicTask(sub.getId());
                subTasks.put(sub.getId(), sub);
                epicTasks.get(key).addSubTasks(sub);
            }
        }


    }
    @SuppressWarnings("rawtypes")
    public List getListOfEpic(){
        List <Epic> epics = new ArrayList<>();
        for(Integer key: epicTasks.keySet()){
            epics.add(epicTasks.get(key));
            System.out.println(epicTasks.get(key));
        }
        return epics;
    }
    @SuppressWarnings("rawtypes")
    public List getListOfTask(){
        List <Task> taskss = new ArrayList<>();

        for(Integer key: tasks.keySet()){
            taskss.add(epicTasks.get(key));

        }
        return taskss;
    }
    @SuppressWarnings("rawtypes")
    public List getListOfSub(){
        List<SubTask> subTaskss = new ArrayList<>();
        for(Integer key: subTasks.keySet()){
            subTaskss.add(subTasks.get(key));

        }
        return subTaskss;
    }
    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }
    public void updateEpicTask(int id) {
        SubTask subTask = (subTasks.get(id));
                if(subTask != null) {
                    Epic epic = subTask.getEpicObject();
                    String statusOfEpic;
                    boolean doneTrue = false;
                    boolean newTrue = false;
                    for (SubTask sub : epic.getSubTasks()) {
                        if (sub.status != null) {
                            if (sub.status.equals("NEW")) {
                                newTrue = true;
                            } else if (sub.status.equals("DONE")) {
                                doneTrue = true;
                            }
                        }
                    }
                    if (doneTrue && !newTrue) {
                        statusOfEpic = "DONE";
                    } else if (!doneTrue && newTrue || epic.getSubTasks().isEmpty()) {
                        statusOfEpic = "NEW";
                    }
                    else{
                        statusOfEpic = "IN_PROGRESS";
                    }
                    epic.status = statusOfEpic;
                    epicTasks.put(epic.getId(), epic);
                }
    }
    public void updateSubTask(SubTask subTask){

         subTasks.put(subTask.getId(), subTask);
        updateEpicTask(subTask.getId());
    }
    public SubTask getSubTask(int id){
        return subTasks.get(id);
    }
    public Epic getEpicTask(int id){
        if(!epicTasks.isEmpty()) {
            return epicTasks.get(id);
        }
        return null;
    }
    public Task getTask(int id){
        if(!tasks.isEmpty()) {
            return tasks.get(id);
        }
        return null;
    }
    public void clearTasks(){
        tasks.clear();
    }
    public void clearEpicTasks(){
        epicTasks.clear();
    }
    public void clearSubTasks(){
        subTasks.clear();
    }
    public void removeTask(int id){
        tasks.remove(id);
    }
    public void removeSubTask(int id){
        subTasks.remove(id);
        SubTask subTask = subTasks.get(id);
        updateEpicTask(subTask.getId());
    }
    public void removeEpicTask(int id){

        ArrayList<Integer> keys = new ArrayList<>();

        for(Integer key : subTasks.keySet()){
            SubTask sub = subTasks.get(key);
            if(sub.getEpicObject().equals(epicTasks.get(id))){
               keys.add(key);
            }
        }
        for(Integer key : keys){
            subTasks.remove(key);
        }
        epicTasks.remove(id);



    }



    @SuppressWarnings("rawtypes")
    public ArrayList getSubsOfEpic(int id){
        Epic epic = epicTasks.get(id);
        return epic.getSubTasks();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Manager{" + "idOfAll=" + idOfAll + "\n" + "Tasks=" + "\n");
        for(Integer key : tasks.keySet()){
            result.append(tasks.get(key)).append("\n");
        }
        result.append("SubTask=" + "\n");
        for(Integer key : subTasks.keySet()){
            result.append(subTasks.get(key)).append("\n");
        }
        result.append("EpicTasks=").append("\n");
        for(Integer key : epicTasks.keySet()){
            result.append(epicTasks.get(key)).append("\n");
        }

        return result.toString();
    }
}




