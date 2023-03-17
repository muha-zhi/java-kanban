package taskTrecker.tasks;

import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {
    public Epic(int id) {
        super(id);
    }

    public Epic() {
        super();
    }

    private List<SubTask> subsOfThisEpic = new ArrayList<>();


    public List<SubTask> getSubsOfThisEpic() {
        return subsOfThisEpic;
    }

    public void addSubForEpic(SubTask s) {
        subsOfThisEpic.add(s);
    }

    public void setSubsOfThisEpic(List<SubTask> subsOfThisEpic) {
        this.subsOfThisEpic = subsOfThisEpic;
    }
}





