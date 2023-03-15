package taskTrecker.tasks;


public class Task {
    protected String name;
    protected String description;
    protected StatusOfTask status = StatusOfTask.NEW;
    private int id = 0;



    public StatusOfTask getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return  name.equals(task.name) && description.equals(task.description) && status.equals(task.status);
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
