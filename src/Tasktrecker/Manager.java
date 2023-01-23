package Tasktrecker;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Manager {
    private int idOfAll = 0;


    public int getIdOfAll() {
        idOfAll += 1;
        return idOfAll;
    }



   private HashMap<Integer, Task> Tasks = new HashMap<>();
   private HashMap<Integer, SubTask> SubTasks = new HashMap<>();

    public HashMap<Integer, Task> getTasks() {
        return Tasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return SubTasks;
    }

    public HashMap<Integer, Epic> getEpicTasks() {
        return EpicTasks;
    }

    private HashMap<Integer, Epic> EpicTasks = new HashMap<>();
    public void doTask(Task task){
        task.status = "NEW";
        task.setId(getIdOfAll());
        Tasks.put(task.getId(), task);
    }
    public void doEpicTask(Epic epic){
        epic.status = "NEW";
        epic.setId(getIdOfAll());
        EpicTasks.put(epic.getId(), epic);
    }

    public void doSubTask(SubTask sub){
        sub.setId(getIdOfAll());
        for(Integer key : EpicTasks.keySet()){
            Epic someEpic = EpicTasks.get(key);
            if (someEpic.equals(sub.getEpicObject())){
                sub.setEpicObject(someEpic);
                updateEpicTask(sub.getId());
                SubTasks.put(sub.getId(), sub);
                someEpic.addSubTasks(sub);
            }
        }


    }
    @SuppressWarnings("rawtypes")
    public List getListOfEpic(){
        List <Epic> epics = new ArrayList<>();
        for(Integer key: EpicTasks.keySet()){
            epics.add(EpicTasks.get(key));
            System.out.println(EpicTasks.get(key));
        }
        return epics;
    }
    @SuppressWarnings("rawtypes")
    public List getListOfTask(){
        List <Task> tasks = new ArrayList<>();
        for(Integer key: Tasks.keySet()){
            tasks.add(EpicTasks.get(key));

        }
        return tasks;
    }
    @SuppressWarnings("rawtypes")
    public List getListOfSub(){
        List<SubTask> subTasks = new ArrayList<>();
        for(Integer key: SubTasks.keySet()){
            subTasks.add(SubTasks.get(key));

        }
        return subTasks;
    }
    public void updateTask(Task task){
        Tasks.put(task.getId(), task);
    }
    public void updateEpicTask(int id) {
        SubTask subTask = (SubTasks.get(id));
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
                    if (doneTrue & !newTrue) {
                        statusOfEpic = "DONE";
                    } else if (!doneTrue & newTrue || epic.getSubTasks().isEmpty()) {
                        statusOfEpic = "NEW";
                    }
                    else{
                        statusOfEpic = "IN_PROGRESS";
                    }
                    epic.status = statusOfEpic;
                    EpicTasks.put(epic.getId(), epic);
                }
    }
    public void updateSubTask(SubTask subTask){

         SubTasks.put(subTask.getId(), subTask);
        updateEpicTask(subTask.getId());
    }
    public SubTask getSubTask(int id){
        if(!SubTasks.isEmpty()) {
            return SubTasks.get(id);
        }
        return null;
    }
    public Epic getEpicTask(int id){
        if(!EpicTasks.isEmpty()) {
            return EpicTasks.get(id);
        }
        return null;
    }
    public Task getTask(int id){
        if(!Tasks.isEmpty()) {
            return Tasks.get(id);
        }
        return null;
    }
    public void clearTasks(){
        Tasks.clear();
    }
    public void clearEpicTasks(){
        EpicTasks.clear();
    }
    public void clearSubTasks(){
        SubTasks.clear();
    }
    public void removeTask(int id){
        Tasks.remove(id);
    }
    public void removeSubTask(int id){
        SubTasks.remove(id);
        SubTask subTask = SubTasks.get(id);
        updateEpicTask(subTask.getId());
    }
    public void removeEpicTask(int id){
        Epic epic = EpicTasks.get(id);
        ArrayList<Integer> keys = new ArrayList<>();

        for(Integer key : SubTasks.keySet()){
            SubTask sub = SubTasks.get(key);
            if(sub.getEpicObject().equals(epic)){
               keys.add(key);
            }
        }
        for(Integer key : keys){
            SubTasks.remove(key);
        }



    }



    @SuppressWarnings("rawtypes")
    public ArrayList getSubsOfEpic(int id){
        Epic epic = EpicTasks.get(id);
        return epic.getSubTasks();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Manager{" + "idOfAll=" + idOfAll + "\n" + "Tasks=" + "\n");
        for(Integer key : Tasks.keySet()){
            result.append(Tasks.get(key)).append("\n");
        }
        result.append("SubTask=" + "\n");
        for(Integer key : SubTasks.keySet()){
            result.append(SubTasks.get(key)).append("\n");
        }
        result.append("EpicTasks=").append("\n");
        for(Integer key : EpicTasks.keySet()){
            result.append(EpicTasks.get(key)).append("\n");
        }

        return result.toString();
    }
}




