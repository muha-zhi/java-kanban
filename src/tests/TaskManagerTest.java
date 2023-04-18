package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTrecker.tasks.Epic;
import taskTrecker.tasks.SubTask;
import taskTrecker.tasks.Task;
import taskTrecker.tasks.manager.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    TaskManager manager;

    public TaskManagerTest(TaskManager manager) {

        this.manager = manager;

    }


    @BeforeEach
    public void afterEach() throws IOException, InterruptedException {
        manager.clearEpicTasks();
        manager.clearTasks();
        manager.clearSubTasks();

        manager.setIdOfAll(0);
    }


    @Test
    public void shouldDoTaskWhenMapIsEmpty() throws IOException, InterruptedException {
        Task task = new Task("name", "dis");
        assertTrue(manager.getListOfTask().isEmpty());
        manager.doTask(task);
        List<Task> tasks = manager.getListOfTask();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

    @Test
    public void shouldDoTaskInStandard() throws IOException, InterruptedException {
        Task task = new Task("name", "dis");
        manager.doTask(task);
        assertFalse(manager.getListOfTask().isEmpty());
        Task task2 = new Task("name2", "dis2");
        manager.doTask(task2);
        List<Task> tasks = manager.getListOfTask();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }


    @Test
    public void shouldDoEpicWhenMapIsEmpty() throws IOException, InterruptedException {
        Epic task = new Epic("name", "dis");
        assertTrue(manager.getListOfEpic().isEmpty());
        manager.doEpicTask(task);
        List<Epic> epics = manager.getListOfEpic();
        assertNotNull(epics);
        assertEquals(1, epics.size());
    }

    @Test
    public void shouldDoEpicInStandard() throws IOException, InterruptedException {
        Epic task = new Epic("name", "dis");
        manager.doEpicTask(task);
        assertFalse(manager.getListOfEpic().isEmpty());
        Epic task2 = new Epic("name2", "dis2");
        manager.doEpicTask(task2);
        List<Epic> tasks = manager.getListOfEpic();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }


    @Test
    public void shouldDoSubTaskWhenMapIsEmpty() throws IOException, InterruptedException {
        SubTask task = new SubTask("name", "dis");
        assertTrue(manager.getListOfSub().isEmpty());
        manager.doSubTask(task);
        List<SubTask> tasks = manager.getListOfSub();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

    @Test
    public void shouldDoSubTaskInStandard() throws IOException, InterruptedException {
        SubTask task = new SubTask("name", "dis");
        manager.doSubTask(task);
        assertFalse(manager.getListOfSub().isEmpty());
        SubTask task2 = new SubTask("name2", "dis2");
        manager.doSubTask(task2);
        List<SubTask> tasks = manager.getListOfSub();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }


    @Test
    public void shouldReturnEmptyListOfTaskIfMapIsEmpty() {

        assertTrue(manager.getListOfTask().isEmpty());

    }

    @Test
    public void shouldReturnListOfTaskInStandard() throws IOException, InterruptedException {

        Task task = new Task("name", "dis", 1);

        manager.doTask(task);
        List<Task> tasks = manager.getListOfTask();

        assertEquals(new Task("name", "dis", 1), tasks.get(0));

    }

    @Test
    public void shouldReturnEmptyListOfEpicIfMapIsEmpty() {

        assertTrue(manager.getListOfEpic().isEmpty());

    }

    @Test
    public void shouldReturnListOfEpicInStandard() throws IOException, InterruptedException {

        Epic task = new Epic("name", "dis", 1);

        manager.doEpicTask(task);
        List<Epic> tasks = manager.getListOfEpic();

        assertEquals(new Epic("name", "dis", 1), tasks.get(0));

    }

    @Test
    public void shouldReturnEmptyListOfSubTaskIfMapIsEmpty() {

        assertTrue(manager.getListOfSub().isEmpty());

    }

    @Test
    public void shouldReturnListOfSubTaskInStandard() throws IOException, InterruptedException {

        SubTask task = new SubTask("name", "dis", 1);

        manager.doSubTask(task);
        List<SubTask> tasks = manager.getListOfSub();

        assertEquals(new SubTask("name", "dis", 1), tasks.get(0));

    }

    @Test
    public void shouldUpdateTaskInStandard() throws IOException, InterruptedException {
        Task task = new Task("name", "dis", 1);
        manager.doTask(task);

        Task taskForUpdate = new Task("nnnn", "ddddd", 1);
        manager.updateTask(taskForUpdate);
        assertEquals(1, manager.getListOfTask().size());
        assertEquals(manager.getTask(1), taskForUpdate);
    }

    @Test
    public void shouldNotUpdateTaskWhenIdEqualsNull() {
        final IllegalStateException exception = assertThrows(

                // класс ошибки
                IllegalStateException.class,

                // создание и переопределение экземпляра класса Executable
                () -> {
                    Task task = new Task("name", "dis", 0);

                    // здесь блок кода, который хотим проверить
                    // при делении на 0 ожидаем ArithmeticException
                    manager.updateTask(task);


                });
        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    public void shouldUpdateSubTaskInStandard() throws IOException, InterruptedException {
        SubTask task = new SubTask("name", "dis", 1);
        manager.doSubTask(task);

        SubTask taskForUpdate = new SubTask("nnnn", "ddddd", 1);
        manager.updateSubTask(taskForUpdate);
        assertEquals(1, manager.getListOfSub().size());
        assertEquals(manager.getSubTask(1), taskForUpdate);
    }

    @Test
    public void shouldNotUpdateSubTaskWhenIdEqualsNull() {
        final IllegalStateException exception = assertThrows(

                // класс ошибки
                IllegalStateException.class,

                // создание и переопределение экземпляра класса Executable
                () -> {
                    SubTask task = new SubTask("name", "dis", 0);

                    // здесь блок кода, который хотим проверить
                    // при делении на 0 ожидаем ArithmeticException
                    manager.updateSubTask(task);


                });
        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    public void shouldUpdateEpicTaskInStandard() throws IOException, InterruptedException {
        Epic task = new Epic("name", "dis", 1);
        manager.doEpicTask(task);

        Epic taskForUpdate = new Epic("nnnn", "ddddd", 1);
        manager.updateEpicTask(taskForUpdate);
        assertEquals(1, manager.getListOfEpic().size());
        assertEquals(manager.getEpicTask(1), taskForUpdate);
    }

    @Test
    public void shouldNotUpdateEpicTaskWhenIdEqualsNull() {
        final IllegalStateException exception = assertThrows(

                // класс ошибки
                IllegalStateException.class,

                // создание и переопределение экземпляра класса Executable
                () -> {
                    Epic task = new Epic("name", "dis", 0);

                    // здесь блок кода, который хотим проверить
                    // при делении на 0 ожидаем ArithmeticException
                    manager.updateEpicTask(task);


                });
        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    public void shouldRemoveTaskInStandard() throws IOException, InterruptedException {
        manager.doTask(new Task("name", "dis", 1));
        assertEquals(new Task("name", "dis", 1), manager.getTask(1));
        manager.removeTask(1);
        assertNull(manager.getTask(1));

    }

    @Test
    public void shouldNotRemoveTaskIfIdEqualsNull() {
        final IllegalStateException exception = assertThrows(

                // класс ошибки
                IllegalStateException.class,

                // создание и переопределение экземпляра класса Executable
                () -> {


                    // здесь блок кода, который хотим проверить
                    // при делении на 0 ожидаем ArithmeticException
                    manager.removeTask(0);


                });
        assertEquals("Задача не найдена.", exception.getMessage());

    }

    @Test
    public void shouldRemoveEpicTaskInStandard() throws IOException, InterruptedException {
        manager.doEpicTask(new Epic("name", "dis", 1));
        assertEquals(new Epic("name", "dis", 1), manager.getEpicTask(1));
        manager.removeEpicTask(1);
        assertNull(manager.getEpicTask(1));

    }

    @Test
    public void shouldNotRemoveEpicTaskIfIdEqualsNull() {
        final IllegalStateException exception = assertThrows(

                // класс ошибки
                IllegalStateException.class,

                // создание и переопределение экземпляра класса Executable
                () -> {


                    // здесь блок кода, который хотим проверить
                    // при делении на 0 ожидаем ArithmeticException
                    manager.removeEpicTask(0);


                });
        assertEquals("Задача не найдена.", exception.getMessage());

    }

    @Test
    public void shouldRemoveSubTaskInStandard() throws IOException, InterruptedException {
        manager.doSubTask(new SubTask("name", "dis", 1));
        assertEquals(new SubTask("name", "dis", 1), manager.getSubTask(1));
        manager.removeSubTask(1);
        assertNull(manager.getSubTask(1));

    }

    @Test
    public void shouldNotRemoveSubTaskIfIdEqualsNull() {
        final IllegalStateException exception = assertThrows(

                // класс ошибки
                IllegalStateException.class,

                // создание и переопределение экземпляра класса Executable
                () -> {


                    // здесь блок кода, который хотим проверить
                    // при делении на 0 ожидаем ArithmeticException
                    manager.removeSubTask(0);


                });
        assertEquals("Задача не найдена.", exception.getMessage());

    }

    @Test
    public void shouldClearMapOfTasksInStandard() throws IOException, InterruptedException {
        manager.doTask(new Task("name", "dis"));
        assertEquals(1, manager.getListOfTask().size());
        manager.clearTasks();
        assertEquals(0, manager.getListOfTask().size());
    }

    @Test
    public void shouldClearMapOfTasksWhenSizeNull() throws IOException, InterruptedException {

        assertEquals(0, manager.getListOfTask().size());
        manager.clearTasks();
        assertEquals(0, manager.getListOfTask().size());
    }

    @Test
    public void shouldClearMapOfSubTaskTasksInStandard() throws IOException, InterruptedException {
        manager.doSubTask(new SubTask("name", "dis"));
        assertEquals(1, manager.getListOfSub().size());
        manager.clearSubTasks();
        assertEquals(0, manager.getListOfSub().size());
    }

    @Test
    public void shouldClearMapOfSubTasksWhenSizeNull() throws IOException {

        assertEquals(0, manager.getListOfSub().size());
        manager.clearSubTasks();
        assertEquals(0, manager.getListOfSub().size());
    }

    @Test
    public void shouldClearMapOfEpicTaskTasksInStandard() throws IOException, InterruptedException {
        manager.doEpicTask(new Epic("name", "dis"));
        assertEquals(1, manager.getListOfEpic().size());
        manager.clearEpicTasks();
        assertEquals(0, manager.getListOfEpic().size());
    }

    @Test
    public void shouldClearMapOfEpicTasksWhenSizeNull() throws IOException, InterruptedException {

        assertEquals(0, manager.getListOfEpic().size());
        manager.clearEpicTasks();
        assertEquals(0, manager.getListOfEpic().size());
    }

    @Test
    public void shouldNotDoTaskIfIsIntersects() throws IOException, InterruptedException {
        manager.doTask(new Task("name", "dis", LocalDateTime.of(2023, 1, 1
                , 12, 0), Duration.ofMinutes(15)));
        assertEquals(1, manager.getListOfTask().size());
        manager.doTask(new Task("name", "dis", LocalDateTime.of(2023, 1, 1
                , 12, 0), Duration.ofMinutes(13)));
        assertEquals(1, manager.getListOfTask().size());

    }


}