package tests;

import taskTrecker.tasks.manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest {
    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }


}
