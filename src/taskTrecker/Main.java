package taskTrecker;

import taskTrecker.Tasks.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        System.out.println(manager);


    }
}
