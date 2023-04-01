package taskTrecker.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTrecker.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    HistoryManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldRemoveHistoryInStandard() {
        manager.add(new Task("name", "dis", 1));
        assertEquals(1, manager.getHistory().size());
        manager.remove(new Task("name", "dis", 1));
        assertEquals(0, manager.getHistory().size());
    }

    @Test
    public void shouldRemoveFirstHistory() {
        manager.add(new Task("name", "dis", 1));
        manager.add(new Task("name2", "dis2", 2));
        manager.add(new Task("name3", "dis3", 3));
        assertEquals(3, manager.getHistory().size());
        manager.remove(new Task("name", "dis", 1));
        List<Task> tasks = manager.getHistory();
        assertEquals(2, manager.getHistory().size());
        assertEquals(new Task("name2", "dis2", 2), tasks.get(0));
        assertEquals(new Task("name3", "dis3", 3), tasks.get(1));
    }

    @Test
    public void shouldRemoveInTheMiddleHistory() {
        manager.add(new Task("name", "dis", 1));


        manager.add(new Task("name2", "dis2", 7));
        manager.add(new Task("name3", "dis3", 3));
        assertEquals(3, manager.getHistory().size());
        manager.remove(new Task("name2", "dis2", 7));
        List<Task> tasks = manager.getHistory();

        assertEquals(2, tasks.size());

    }

    @Test
    public void shouldRemoveInLastHistory() {
        manager.add(new Task("name", "dis", 1));


        manager.add(new Task("name2", "dis2", 7));
        manager.add(new Task("name3", "dis3", 3));
        assertEquals(3, manager.getHistory().size());
        manager.remove(new Task("name2", "dis2", 3));
        List<Task> tasks = manager.getHistory();

        assertEquals(2, tasks.size());

    }

    @Test
    public void shouldNotDuplicateWhenAdd() {
        manager.add(new Task("name", "dis", 1));
        manager.add(new Task("name", "dis", 1));
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void shouldAddInStandard() {
        manager.add(new Task("name", "dis", 1));
        manager.add(new Task("name3", "dis4", 2));
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    public void shouldReturnListOfHistory() {
        manager.add(new Task("name", "dis", 1));
        manager.add(new Task("name3", "dis4", 2));

        List<Task> tasks = List.of(new Task("name", "dis", 1)
                , new Task("name3", "dis4", 2));

        List<Task> returnTasks = manager.getHistory();

        assertEquals(tasks.get(0), returnTasks.get(0));
        assertEquals(tasks.get(1), returnTasks.get(1));
    }

}