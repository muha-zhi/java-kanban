package taskTrecker.Tasks;


public class Task {
    protected String name;
    protected String description;
    protected StatusOfTask status = StatusOfTask.NEW;
    private int id;

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

    public StatusOfTask isStatus() {
        return status;
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
        // умножаем промежуточный результат на простое число

        if (name != null) {
            // вычисляем хеш второго поля и добавляем его к общему результату
            hash = hash + name.hashCode();
        }
        hash = hash * 31;
        if (description != null) {
            // вычисляем хеш первого поля и добавляем к нему начальное значение
            hash = hash + description.hashCode();
        }
        hash = hash * 31;
        if (status != null) {
            // вычисляем хеш первого поля и добавляем к нему начальное значение
            hash = hash + status.hashCode();
        }


        return hash; // возвращаем итоговый хеш
    }
}
