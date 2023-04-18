package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTrecker.tasks.Epic;
import taskTrecker.tasks.SubTask;
import taskTrecker.tasks.Task;
import taskTrecker.tasks.manager.HttpTaskManager;
import taskTrecker.tasks.manager.Managers;
import taskTrecker.tasks.server.KVServer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest {


    KVServer server = new KVServer();

    HttpTaskManager manager1 = Managers.getHttpTasksManager();


    public HttpTaskManagerTest() throws IOException, InterruptedException {


        super(Managers.getHttpTasksManager());


    }


    @BeforeEach
    public void startServer() throws IOException, InterruptedException {

        server.start();


    }

    @AfterEach
    public void afterEach() throws IOException, InterruptedException {

        manager.clearEpicTasks();
        manager.clearTasks();
        manager.clearSubTasks();
        manager.setIdOfAll(0);
        manager1.historyManager.clearHistory();
        manager1.save();
        server.stop();


    }


    @Test
    public void shouldLoadFromServerEmptyListOfEpic() throws IOException {
        Epic epic = new Epic("name", "dis", LocalDateTime.of(2023, 1
                , 1, 12, 0), Duration.ofMinutes(10));
        manager1.doEpicTask(epic);
        assertEquals(1, manager1.getListOfEpic().size());
        assertEquals(0, manager1.getEpicTask(epic.getId()).getSubsOfThisEpic().size());
        HttpTaskManager manager = HttpTaskManager.load("key123");
        if (manager != null) {
            assertEquals(1, manager.getListOfEpic().size());
            assertEquals(0, manager.getEpicTask(epic.getId()).getSubsOfThisEpic().size());
        }

    }

    @Test
    public void shouldLoadFromServerEmptyIfHistoryEmpty() throws IOException, InterruptedException {

        manager1.doEpicTask(new Epic("name", "dis", LocalDateTime.of(2023, 1
                , 1, 12, 0), Duration.ofMinutes(10)));
        manager1.doEpicTask(new Epic("name2", "dis2", LocalDateTime.of(2023, 1
                , 1, 13, 0), Duration.ofMinutes(10)));


        assertEquals(2, manager1.getListOfEpic().size());
        assertEquals(0, manager1.historyManager.getHistory().size());

        HttpTaskManager manager = HttpTaskManager.load("key123");
        if (manager != null) {
            assertEquals(2, manager.getListOfEpic().size());
            assertEquals(0, manager.historyManager.getHistory().size());
        }

    }

    @Test
    public void shouldLoadFromServerInStandard() throws IOException, InterruptedException {


        HttpTaskManager manager = Managers.getHttpTasksManager();
        manager.doSubTask(new SubTask("name2", "dis2", 1));


        manager.doEpicTask(new Epic("name2", "dis2", 2));
        manager.doTask(new Task("name2", "dis2", 3));


        manager.getEpicTask(2);
        manager.getSubTask(1);
        manager.getTask(3);

        HttpTaskManager manager2 = HttpTaskManager.load("key123");
        if (manager2 != null) {
            assertEquals(3, manager2.historyManager.getHistory().size());
        }


    }
}
