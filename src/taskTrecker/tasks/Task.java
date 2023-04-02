package taskTrecker.tasks;


import taskTrecker.tasks.manager.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task  {

    protected String name;
    protected String description;
    protected StatusOfTask status = StatusOfTask.NEW;

    protected LocalDateTime startTime;
    protected Duration duration;


    private int id = 0;


    public Task(String name, String description, StatusOfTask status, LocalDateTime startTime, Duration duration, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.id = id;
    }
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = Managers.getDefault().getIdOfAll();
    }

    public Task(int id) {

        this.id = id;
    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
        this.id = Managers.getDefault().getIdOfAll();
    }


    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }




    public LocalDateTime getEndTime(){
        LocalDateTime time = null;
        if(startTime != null && duration != null) {
            time = startTime.plusMinutes(duration.toMinutes());
        }
       return time;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {

            return startTime;


    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }


    public StatusOfTask getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setStatus(StatusOfTask status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", status='" + status + '\''
                + ", id=" + id + '}' + " ";
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.getId();
    }

    @Override
    public int hashCode() {
        int hash = 17;


        if (name != null) {

            hash = hash + name.hashCode();
        }
        hash = hash * 31;
        if (description != null) {

            hash = hash + description.hashCode();
        }
        hash = hash * 31;
        if (status != null) {

            hash = hash + status.hashCode();
        }


        return hash;
    }
}
