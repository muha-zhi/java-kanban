package taskTrecker.tasks;


public class SubTask extends Task {

    private  int epicObject;


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
        return this.epicObject == epicObject;
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

