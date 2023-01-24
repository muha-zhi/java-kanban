package taskTrecker;

import java.util.ArrayList;


public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTasks(SubTask subTask) {
        subTasks.add(subTask);
    }


}





