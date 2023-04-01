package taskTrecker.tasks;

import taskTrecker.tasks.manager.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {

    private List<SubTask> subsOfThisEpic = new ArrayList<>();

    private LocalDateTime endTime;

    public Epic(String name, String description, StatusOfTask status, LocalDateTime startTime, Duration duration,
                int id) {
        super(name, description, status, startTime, duration, id);
    }

    public Epic(String name, String description) {

        super(name, description);
        setStartTime();
        setEndTime();

    }
    public Epic(String name, String description, int id) {
        super(name, description, id);
    }
    public Epic(String name, String description, LocalDateTime startTime, Duration duration) {

        super(name, description, startTime, duration);
        setStartTime();
        setEndTime();

    }



    public void setStartTime() {
        if (subsOfThisEpic.size() != 0) {
            startTime = subsOfThisEpic.get(subsOfThisEpic.size() - 1).getStartTime();
        }

    }

    public void setEndTime() {
        if (subsOfThisEpic.size() != 0) {

            endTime = subsOfThisEpic.get(0).getEndTime();
        }
    }

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





