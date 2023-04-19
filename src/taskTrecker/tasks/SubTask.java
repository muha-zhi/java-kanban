package taskTrecker.tasks;


import com.google.gson.annotations.Expose;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    @Expose
    private int epicObject;

    public SubTask(String name, String description, LocalDateTime startTime, Duration duration
            , int epicObject, int id) {
        super(name, description, startTime, duration, id);
        this.epicObject = epicObject;
    }

    public SubTask() {
        super();
    }

    public SubTask(String name, String description) {
        super(name, description);
    }

    public SubTask(int id) {

        super(id);
    }

    public SubTask(String name, String description, LocalDateTime startTime, Duration duration, int epicObject) {
        super(name, description, startTime, duration);
        this.epicObject = epicObject;

    }

    public SubTask(String name, String description, int id) {
        super(name, description, id);
    }


    public int getEpicObject() {
        return epicObject;
    }

    public void setEpicObject(int epicObject) {
        this.epicObject = epicObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicObject == subTask.epicObject;
    }

    @Override
    public String toString() {
        return super.toString() + '\'' + "epicObject=" + epicObject;


    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash *= 31;
        hash += epicObject;
        return hash;
    }


}

