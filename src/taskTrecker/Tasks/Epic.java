package taskTrecker.Tasks;

import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {
    private List<SubTask> subTasks = new ArrayList<>();

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTasks(SubTask subTask) {
        subTasks.add(subTask);
    }


}





