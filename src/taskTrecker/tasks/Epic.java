package taskTrecker.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class Epic extends Task {

    private List<SubTask> subsOfThisEpic = new ArrayList<>();

    private Set<SubTask> sortedSubsOfThisEpic = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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

    public Epic(int id) {

        super(id);
    }

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    public Epic(String name, String description, LocalDateTime startTime, Duration duration) {

        super(name, description, startTime, duration);
        setStartTime();
        setEndTime();

    }

    public LocalDateTime getEndTime() {
        return endTime;
    }


    public void setStartTime() {
        LocalDateTime startTimeR = null;
        if (subsOfThisEpic.size() != 0) {
            startTimeR = subsOfThisEpic.get(0).getStartTime();

            for (SubTask s : sortedSubsOfThisEpic) {
                if (s.getStartTime().isBefore(startTimeR)) {
                    startTime = s.getStartTime();
                }
            }
        }
        startTime = startTimeR;

    }

    public void setEndTime() {

        LocalDateTime endTimeR = null;
        if (subsOfThisEpic.size() != 0) {

            endTimeR = subsOfThisEpic.get(0).getEndTime();

            if (subsOfThisEpic.size() != 0) {
                for (SubTask s : sortedSubsOfThisEpic) {
                    if (s.getEndTime().isAfter(endTimeR)) {
                        endTimeR = s.getEndTime();
                    }
                }
            }
        }
        endTime = endTimeR;
    }

    public List<SubTask> getSubsOfThisEpic() {
        return subsOfThisEpic;
    }

    public void addSubForEpic(SubTask s) {

        subsOfThisEpic.add(s);
        sortedSubsOfThisEpic.add(s);
    }

    public void setSubsOfThisEpic(List<SubTask> subsOfThisEpic) {
        this.subsOfThisEpic = subsOfThisEpic;
    }
}





