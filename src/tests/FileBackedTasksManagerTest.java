package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTrecker.history.InMemoryHistoryManager;
import taskTrecker.tasks.Epic;
import taskTrecker.tasks.SubTask;
import taskTrecker.tasks.Task;
import taskTrecker.tasks.manager.FileBackedTasksManager;
import taskTrecker.tasks.manager.Managers;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest {

    FileBackedTasksManager fileManager = Managers.getFileManager();

    public FileBackedTasksManagerTest() {
        super(Managers.getFileManager());
    }

    public File getRes() {
        return new File("src" +
                "\\taskTrecker\\resources\\res.csv").getAbsoluteFile();
    }

    @BeforeEach
    public void BeforeEach() throws IOException, InterruptedException {
        fileManager.historyManager.clearHistory();

        manager.clearEpicTasks();
        manager.clearTasks();
        manager.clearSubTasks();
        manager.setIdOfAll(0);
        fileManager.save();
    }


    @AfterEach
    public void afterEach() throws IOException, InterruptedException {
        fileManager.historyManager.clearHistory();

        fileManager.clearEpicTasks();
        fileManager.clearTasks();
        fileManager.clearSubTasks();
        fileManager.setIdOfAll(0);
        fileManager.save();
    }

    @Test
    public void shouldLoadFromFileEmptyMaps() throws IOException, InterruptedException {


        fileManager.save();
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(getRes());
        assertEquals(0, manager.getListOfTask().size());

    }

    @Test
    public void shouldLoadFromFileEmptyListOfEpic() throws IOException {
        Epic epic = new Epic("name", "dis", LocalDateTime.of(2023, 1
                , 1, 12, 0), Duration.ofMinutes(10));
        fileManager.doEpicTask(epic);
        assertEquals(1, fileManager.getListOfEpic().size());
        assertEquals(0, fileManager.getEpicTask(epic.getId()).getSubsOfThisEpic().size());
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(getRes());
        assertEquals(1, manager.getListOfEpic().size());
        assertEquals(0, manager.getEpicTask(epic.getId()).getSubsOfThisEpic().size());

    }

    @Test
    public void shouldLoadFromFileEmptyIfHistoryEmpty() throws IOException {

        fileManager.doEpicTask(new Epic("name", "dis", LocalDateTime.of(2023, 1
                , 1, 12, 0), Duration.ofMinutes(10)));
        fileManager.doEpicTask(new Epic("name2", "dis2", LocalDateTime.of(2023, 1
                , 1, 13, 0), Duration.ofMinutes(10)));


        assertEquals(2, fileManager.getListOfEpic().size());
        assertEquals(0, fileManager.historyManager.getHistory().size());

        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(getRes());

        assertEquals(2, manager.getListOfEpic().size());
        assertEquals(0, manager.historyManager.getHistory().size());

    }

    @Test
    public void shouldLoadFromFileInStandard() throws IOException {
        FileBackedTasksManager manager1 = new FileBackedTasksManager(getRes());
        manager1.setHistoryManager(new InMemoryHistoryManager());


        Task task = new Task("nameT", "disT", LocalDateTime.of(2023, 1
                , 1, 12, 0), Duration.ofMinutes(10), 1);

        Epic epic = new Epic("nameE", "disE", LocalDateTime.of(2023, 1
                , 1, 15, 0), Duration.ofMinutes(10), 2);

        SubTask subTask = new SubTask("nameS", "disS", LocalDateTime.of(2023, 1
                , 1, 15, 30), Duration.ofMinutes(10), epic.getId(), 3);

        manager1.doTask(task);
        manager1.doEpicTask(epic);
        manager1.doSubTask(subTask);


        manager1.getSubTask(3);

        assertEquals(1, manager1.historyManager.getHistory().size());
        assertEquals(1, manager1.getListOfTask().size());
        assertEquals(epic.getId(), subTask.getEpicObject());
        assertEquals(subTask, epic.getSubsOfThisEpic().get(0));

        FileBackedTasksManager manager2 = FileBackedTasksManager.loadFromFile(getRes());


        assertEquals(1, manager2.historyManager.getHistory().size());
        assertEquals(1, manager2.getListOfTask().size());
        assertEquals(epic.getId(), subTask.getEpicObject());
        assertEquals(subTask, epic.getSubsOfThisEpic().get(0));


    }
}
