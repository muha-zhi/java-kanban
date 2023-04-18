package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import taskTrecker.tasks.Epic;
import taskTrecker.tasks.SubTask;
import taskTrecker.tasks.Task;
import taskTrecker.tasks.manager.FileBackedTasksManager;
import taskTrecker.tasks.manager.Managers;
import taskTrecker.tasks.server.DateAdapter;
import taskTrecker.tasks.server.DurationAdapter;
import taskTrecker.tasks.server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {


    HttpClient httpClient = HttpClient.newHttpClient();

    Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new DateAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();


    static HttpTaskServer server;

    FileBackedTasksManager manager = Managers.getFileManager();


    @BeforeAll
    public static void start() throws IOException, InterruptedException {
        server = new HttpTaskServer();
        server.start();
    }


    @AfterEach
    public void afterEach() throws IOException, InterruptedException {
        Managers.getDefaultHistory().clearHistory();
        manager.setIdOfAll(0);
        manager.clearEpicTasks();
        manager.clearTasks();
        manager.clearSubTasks();
        manager.save();


    }

    @AfterAll
    public static void afterAll() {
        server.stop();
    }

    @Test
    public void shouldDoTask() throws IOException, InterruptedException {
        String json = gson.toJson(new Task("name", "dis", 1));

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldDoEpic() throws IOException, InterruptedException {
        String json = gson.toJson(new Epic("name", "dis", 1));

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldDoSub() throws IOException, InterruptedException {
        String json = gson.toJson(new SubTask("name", "dis", 1));

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldReturnTask() throws IOException, InterruptedException {

        manager.doTask(new Task("name", "dis", 1));

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        JsonElement element = JsonParser.parseString(response2.body());

        Task task = gson.fromJson(element, Task.class);


        assertEquals(1, task.getId());
    }

    @Test
    public void shouldReturnEpic() throws IOException, InterruptedException {


        manager.doEpicTask(new Epic("name", "dis", 1));

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/epic?id=1"))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        JsonElement element = JsonParser.parseString(response2.body());

        Epic task = gson.fromJson(element, Epic.class);


        assertEquals(1, task.getId());
    }

    @Test
    public void shouldReturnSub() throws IOException, InterruptedException {


        manager.doSubTask(new SubTask("name", "dis", 1));
        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/subtask?id=1"))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        JsonElement element = JsonParser.parseString(response2.body());

        SubTask task = gson.fromJson(element, SubTask.class);


        assertEquals(1, task.getId());
    }

    @Test
    public void shouldReturnListSub() throws IOException, InterruptedException {

        manager.doSubTask(new SubTask("name", "dis", 1));
        manager.doSubTask(new SubTask("name2", "dis2", 2));

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        JsonElement element = JsonParser.parseString(response2.body());

        List<SubTask> tasks = gson.fromJson(element, List.class);


        assertEquals(2, tasks.size());
    }

    @Test
    public void shouldReturnListTask() throws IOException, InterruptedException {

        manager.doTask(new Task("name", "dis", 1));
        manager.doTask(new Task("name2", "dis2", 2));

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        JsonElement element = JsonParser.parseString(response2.body());

        List<Task> tasks = gson.fromJson(element, List.class);


        assertEquals(2, tasks.size());
    }

    @Test
    public void shouldReturnListEpic() throws IOException, InterruptedException {

        manager.doEpicTask(new Epic("name", "dis", 1));
        manager.doEpicTask(new Epic("name2", "dis2", 2));

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        JsonElement element = JsonParser.parseString(response2.body());

        List<Epic> tasks = gson.fromJson(element, ArrayList.class);


        assertEquals(2, tasks.size());
    }

    @Test
    public void shouldDelEpicById() throws IOException, InterruptedException {

        manager.doEpicTask(new Epic("name", "dis", 1));
        assertEquals(1, manager.getListOfEpic().size());

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/epic?id=1"))
                .build();

        HttpResponse<String> response = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());


        assertEquals(0, manager.getListOfEpic().size());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldDelSubById() throws IOException, InterruptedException {

        manager.doSubTask(new SubTask("name", "dis", 1));
        assertEquals(1, manager.getListOfSub().size());

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/subtask?id=1"))
                .build();

        HttpResponse<String> response = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());


        assertEquals(0, manager.getListOfSub().size());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldDelTaskById() throws IOException, InterruptedException {

        manager.doTask(new Task("name", "dis", 1));
        assertEquals(1, manager.getListOfTask().size());

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .build();

        HttpResponse<String> response = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());


        assertEquals(0, manager.getListOfTask().size());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldClearEpicList() throws IOException, InterruptedException {

        manager.doEpicTask(new Epic("name", "dis", 1));
        manager.doEpicTask(new Epic("name", "dis", 2));
        assertEquals(2, manager.getListOfEpic().size());

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/epic"))
                .build();

        HttpResponse<String> response = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());


        assertEquals(0, manager.getListOfEpic().size());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldReturnHistory() throws IOException, InterruptedException {


        manager.doEpicTask(new Epic("name3234", "dis3535", 2));

        manager.getEpicTask(2);


        assertEquals(1, manager.historyManager.getTasksToRemove().size());

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/history"))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        JsonElement element = JsonParser.parseString(response2.body());

        List<Task> tasks = gson.fromJson(element, ArrayList.class);

        assertEquals(200, response2.statusCode());
        System.out.println(tasks);
        assertEquals(1, tasks.size());

    }

    @Test
    public void shouldClearSubList() throws IOException, InterruptedException {

        manager.doSubTask(new SubTask("name", "dis", 1));
        manager.doSubTask(new SubTask("name", "dis", 2));
        assertEquals(2, manager.getListOfSub().size());

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/subtask"))
                .build();

        HttpResponse<String> response = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());


        assertEquals(0, manager.getListOfSub().size());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldClearTaskList() throws IOException, InterruptedException {

        manager.doTask(new Task("name", "dis", 1));
        manager.doTask(new Task("name", "dis", 2));
        assertEquals(2, manager.getListOfTask().size());

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/task"))
                .build();

        HttpResponse<String> response = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());


        assertEquals(0, manager.getListOfTask().size());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldReturnAllTasks() throws IOException, InterruptedException {

        manager.doSubTask(new SubTask("name", "dis", 1));
        manager.doEpicTask(new Epic("name2", "dis2", 2));
        manager.doTask(new Task("name2", "dis2", 3));

        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .GET()
                .uri(URI.create("http://localhost:8080/tasks"))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        JsonElement element = JsonParser.parseString(response2.body());

        List<Task> tasks = gson.fromJson(element, List.class);

        assertEquals(200, response2.statusCode());
        assertEquals(3, tasks.size());
    }

    @Test
    public void shouldReturnSubOfEpic() throws IOException, InterruptedException {


        manager.doEpicTask(new Epic("name2", "dis2", 2));
        SubTask subTask = new SubTask("name", "dis", 1);
        subTask.setEpicObject(2);
        manager.doSubTask(subTask);


        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .GET()
                .uri(URI.create("http://localhost:8080/tasks/subtask/epic?id=2"))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        JsonElement element = JsonParser.parseString(response2.body());

        List<SubTask> tasks = gson.fromJson(element, List.class);



        assertEquals(200, response2.statusCode());
        assertEquals(1, tasks.size());

    }


    @Test
    public void shouldDelHistory() throws IOException, InterruptedException {


        manager.doEpicTask(new Epic("name2", "dis2", 2));
        SubTask subTask = new SubTask("name", "dis", 1);
        manager.doSubTask(subTask);
        manager.getEpicTask(2);
        manager.getSubTask(1);

        assertEquals(2, manager.historyManager.getHistory().size());
        HttpRequest.Builder requestBuilder2 = HttpRequest.newBuilder();
        HttpRequest request2 = requestBuilder2
                .DELETE()
                .uri(URI.create("http://localhost:8080/tasks/history"))
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());



        assertEquals(0, manager.historyManager.getHistory().size());

        assertEquals(200, response2.statusCode());


    }


}
