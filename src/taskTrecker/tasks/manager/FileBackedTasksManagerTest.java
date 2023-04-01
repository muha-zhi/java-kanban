package taskTrecker.tasks.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTrecker.tasks.Epic;
import taskTrecker.tasks.SubTask;
import taskTrecker.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest {

    FileBackedTasksManager fileBackedTasksManager;

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager(new File("C:/Users" +
                "/mzile/dev/java-kanban/src/taskTrecker/resources/res.csv")));
    }

    @BeforeEach
    public void beforeEach() {
        fileBackedTasksManager = new FileBackedTasksManager(new File("C:/Users" +
                "/mzile/dev/java-kanban/src/taskTrecker/resources/res.csv"));


    }

    @AfterEach
    public void afterEach() throws IOException {
        fileBackedTasksManager.historyManager.clearHistory();
        fileBackedTasksManager.setIdOfAll(0);

        manager.clearEpicTasks();
        manager.clearTasks();
        manager.clearSubTasks();
        manager.setIdOfAll(0);
    }

    @Test
    public void shouldLoadFromFileEmptyMaps() throws IOException {


        fileBackedTasksManager.save();
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("C:/Users" +
                "/mzile/dev/java-kanban/src/taskTrecker/resources/res.csv"));
        assertEquals(0, manager.getListOfTask().size());

    }

    @Test
    public void shouldLoadFromFileEmptyListOfEpic() throws IOException {
        Epic epic = new Epic("name", "dis", LocalDateTime.of(2023, 1
                , 1, 12, 0), Duration.ofMinutes(10));
        fileBackedTasksManager.doEpicTask(epic);
        assertEquals(1, fileBackedTasksManager.getListOfEpic().size());
        assertEquals(0, fileBackedTasksManager.getEpicTask(epic.getId()).getSubsOfThisEpic().size());
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("C:/Users" +
                "/mzile/dev/java-kanban/src/taskTrecker/resources/res.csv"));
        assertEquals(1, manager.getListOfEpic().size());
        assertEquals(0, manager.getEpicTask(epic.getId()).getSubsOfThisEpic().size());

    }

    @Test
    public void shouldLoadFromFileEmptyIfHistoryEmpty() throws IOException {

        fileBackedTasksManager.doEpicTask(new Epic("name", "dis", LocalDateTime.of(2023, 1
                , 1, 12, 0), Duration.ofMinutes(10)));
        fileBackedTasksManager.doEpicTask(new Epic("name2", "dis2", LocalDateTime.of(2023, 1
                , 1, 13, 0), Duration.ofMinutes(10)));

        assertEquals(2, fileBackedTasksManager.getListOfEpic().size());
        assertEquals(0, fileBackedTasksManager.historyManager.getHistory().size());

        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("C:/Users" +
                "/mzile/dev/java-kanban/src/taskTrecker/resources/res.csv"));

        assertEquals(2, manager.getListOfEpic().size());
        assertEquals(0, manager.historyManager.getHistory().size());

    }

    @Test
    public void shouldLoadFromFileInStandard() throws IOException {

        Task task = new Task("nameT", "disT", LocalDateTime.of(2023, 1
                , 1, 12, 0), Duration.ofMinutes(10));

        Epic epic = new Epic("nameE", "disE", LocalDateTime.of(2023, 1
                , 1, 15, 0), Duration.ofMinutes(10));

        SubTask subTask = new SubTask("nameS", "disS", LocalDateTime.of(2023, 1
                , 1, 15, 30), Duration.ofMinutes(10), epic.getId());

        fileBackedTasksManager.doTask(task);
        fileBackedTasksManager.doEpicTask(epic);
        fileBackedTasksManager.doSubTask(subTask);

        fileBackedTasksManager.getTask(task.getId());
        fileBackedTasksManager.getEpicTask(epic.getId());

        assertEquals(2, fileBackedTasksManager.historyManager.getHistory().size());
        assertEquals(1, fileBackedTasksManager.getListOfTask().size());
        assertEquals(epic.getId(), subTask.getEpicObject());
        assertEquals(subTask, epic.getSubsOfThisEpic().get(0));

        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("C:/Users" +
                "/mzile/dev/java-kanban/src/taskTrecker/resources/res.csv"));


        assertEquals(2, manager.historyManager.getHistory().size());
        assertEquals(1, manager.getListOfTask().size());
        assertEquals(epic.getId(), subTask.getEpicObject());
        assertEquals(subTask, epic.getSubsOfThisEpic().get(0));


    }
}
