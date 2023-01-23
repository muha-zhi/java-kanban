package Tasktrecker;



public class Task {
    protected String name;
    protected String description;
    protected String status;
    int id;

    public String getStatus() {
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

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", status='" + status + '\''
                + ", id=" + id + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && description.equals(task.description) && status.equals(task.status);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (id != 0) {
            // вычисляем хеш первого поля и добавляем к нему начальное значение
            hash = hash + id;
        }
        hash = hash * 31; // умножаем промежуточный результат на простое число

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
