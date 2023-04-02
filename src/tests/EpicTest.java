package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTrecker.tasks.Epic;
import taskTrecker.tasks.StatusOfTask;
import taskTrecker.tasks.SubTask;
import taskTrecker.tasks.manager.InMemoryTaskManager;
import taskTrecker.tasks.manager.TaskManager;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    TaskManager manager;
    Epic epic;

    @BeforeEach
    public void beforeEach() throws IOException {
        manager = new InMemoryTaskManager();
        manager.doEpicTask(new Epic("name", "dis"));
        epic = manager.getEpicTask(1);
    }

    @Test
    public void shouldNewStatusIfListEmpty() {
        Assertions.assertEquals(StatusOfTask.NEW, epic.getStatus());
    }

    @Test
    public void shouldNewStatusIfInListOnlyNew() throws IOException {
        if (epic != null) {
            SubTask subTask = new SubTask("Name s", "dis s");
            subTask.setEpicObject(epic.getId());
            SubTask subTask2 = new SubTask("Name s2", "dis s2");
            subTask2.setEpicObject(epic.getId());
            manager.doSubTask(subTask);
            manager.doSubTask(subTask2);
            assertEquals(StatusOfTask.NEW, epic.getStatus());
        }
    }

    @Test
    public void shouldNewStatusIfInListOnlyDone() throws IOException {
        if (epic != null) {
            SubTask subTask = new SubTask("Name s", "dis s");
            subTask.setEpicObject(epic.getId());
            subTask.setStatus(StatusOfTask.DONE);
            SubTask subTask2 = new SubTask("Name s2", "dis s2");
            subTask2.setEpicObject(epic.getId());
            subTask2.setStatus(StatusOfTask.DONE);
            manager.doSubTask(subTask);
            manager.doSubTask(subTask2);
            assertEquals(StatusOfTask.DONE, epic.getStatus());
        }
    }

    @Test
    public void shouldInProgressStatusIfInListDoneAndNew() throws IOException {
        if (epic != null) {
            SubTask subTask = new SubTask("Name s", "dis s");
            subTask.setEpicObject(epic.getId());
            subTask.setStatus(StatusOfTask.NEW);
            SubTask subTask2 = new SubTask("Name s2", "dis s2");
            subTask2.setEpicObject(epic.getId());
            subTask2.setStatus(StatusOfTask.DONE);
            manager.doSubTask(subTask);
            manager.doSubTask(subTask2);
            assertEquals(StatusOfTask.IN_PROGRESS, epic.getStatus());
        }
    }

    @Test
    public void shouldInProgressStatusIfInListInProgress() throws IOException {
        if (epic != null) {
            SubTask subTask = new SubTask("Name s", "dis s");
            subTask.setEpicObject(epic.getId());
            subTask.setStatus(StatusOfTask.IN_PROGRESS);
            SubTask subTask2 = new SubTask("Name s2", "dis s2");
            subTask2.setEpicObject(epic.getId());
            subTask2.setStatus(StatusOfTask.IN_PROGRESS);
            manager.doSubTask(subTask);
            manager.doSubTask(subTask2);
            assertEquals(StatusOfTask.IN_PROGRESS, epic.getStatus());
        }
    }

}