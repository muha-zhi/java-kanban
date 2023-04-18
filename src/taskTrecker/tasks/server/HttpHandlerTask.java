package taskTrecker.tasks.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskTrecker.tasks.Epic;
import taskTrecker.tasks.SubTask;
import taskTrecker.tasks.Task;
import taskTrecker.tasks.manager.FileBackedTasksManager;
import taskTrecker.tasks.manager.Managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static taskTrecker.tasks.server.Endpoint.GET_TASK;

class HttpHandlerTask implements HttpHandler {

    FileBackedTasksManager manager = Managers.getFileManager();


    @Override
    public void handle(HttpExchange h) throws IOException {

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new DateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();


        Endpoint endpoint = getEndpoint(h.getRequestURI(), h.getRequestMethod());

        if (endpoint != null) {
            switch (endpoint) {
                case GET_TASK:
                    String query = h.getRequestURI().getRawQuery();
                    if (query != null) {
                        h.sendResponseHeaders(200, 0);
                        String[] id = query.split("=");
                        Task task = manager.getTask(Integer.parseInt(id[1]));
                        String j = gson.toJson(task);
                        try (OutputStream u = h.getResponseBody()) {
                            u.write(j.getBytes(StandardCharsets.UTF_8));
                        }
                    }
                    break;
                case GET_TASKS:

                    h.sendResponseHeaders(200, 0);

                    List<Task> t = manager.getListOfTask();

                    String j = gson.toJson(t);
                    try (OutputStream u = h.getResponseBody()) {
                        u.write(j.getBytes(StandardCharsets.UTF_8));
                    }
                    break;
                case GET_SUBTASK:
                    String queryS = h.getRequestURI().getRawQuery();
                    if (queryS != null) {
                        h.sendResponseHeaders(200, 0);
                        String[] id = queryS.split("=");
                        SubTask task = manager.getSubTask(Integer.parseInt(id[1]));
                        String g = gson.toJson(task);
                        try (OutputStream u = h.getResponseBody()) {
                            u.write(g.getBytes(StandardCharsets.UTF_8));
                        }
                    }
                    break;
                case GET_SUBTASKS:
                    h.sendResponseHeaders(200, 0);

                    List<SubTask> s = manager.getListOfSub();
                    String g = gson.toJson(s);
                    try (OutputStream u = h.getResponseBody()) {
                        u.write(g.getBytes(StandardCharsets.UTF_8));
                    }
                    break;
                case GET_EPIC:
                    String queryE = h.getRequestURI().getRawQuery();
                    if (queryE != null) {
                        h.sendResponseHeaders(200, 0);
                        String[] id = queryE.split("=");
                        Epic task = manager.getEpicTask(Integer.parseInt(id[1]));
                        String e = gson.toJson(task);
                        try (OutputStream u = h.getResponseBody()) {
                            u.write(e.getBytes(StandardCharsets.UTF_8));
                        }
                    }
                    break;

                case GET_EPICS:
                    h.sendResponseHeaders(200, 0);

                    List<Epic> e = manager.getListOfEpic();
                    String epics = gson.toJson(e);
                    try (OutputStream u = h.getResponseBody()) {
                        u.write(epics.getBytes(StandardCharsets.UTF_8));
                    }
                    break;

                case GET_SUB_EPIC:
                    String queryES = h.getRequestURI().getRawQuery();
                    if (queryES != null) {
                        h.sendResponseHeaders(200, 0);
                        String[] id = queryES.split("=");
                        List<SubTask> subs = manager.getSubsOfEpic(Integer.parseInt(id[1]));
                        String subsJ = gson.toJson(subs);
                        try (OutputStream u = h.getResponseBody()) {
                            u.write(subsJ.getBytes(StandardCharsets.UTF_8));
                        }
                    }
                    break;

                case GET_ALL_TASKS:
                    h.sendResponseHeaders(200, 0);

                    List<Task> all = manager.getPrioritizedTasks();
                    String allTask = gson.toJson(all);
                    try (OutputStream u = h.getResponseBody()) {
                        u.write(allTask.getBytes(StandardCharsets.UTF_8));
                    }
                    break;
                case GET_HISTORY:
                    h.sendResponseHeaders(200, 0);

                    List<Task> history = manager.historyManager.getHistory();
                    String his = gson.toJson(history);
                    try (OutputStream u = h.getResponseBody()) {
                        u.write(his.getBytes(StandardCharsets.UTF_8));
                    }
                    break;

                case POST_TASK:

                    InputStream inputStream = h.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(body, Task.class);

                    manager.doTask(task);


                    if (manager.getListOfTask().contains(task)) {
                        h.sendResponseHeaders(200, 0);
                        h.close();
                    } else {

                        h.sendResponseHeaders(500, 0);
                        return;
                    }

                    break;

                case POST_EPIC:
                    InputStream inputStreamE = h.getRequestBody();
                    String bodyE = new String(inputStreamE.readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = gson.fromJson(bodyE, Epic.class);

                    manager.doEpicTask(epic);


                    if (manager.getListOfEpic().contains(epic)) {
                        h.sendResponseHeaders(200, 0);
                        h.close();
                    } else {

                        h.sendResponseHeaders(500, 0);
                        return;
                    }

                    break;
                case POST_SUBTASK:
                    InputStream inputStreamS = h.getRequestBody();
                    String bodyS = new String(inputStreamS.readAllBytes(), StandardCharsets.UTF_8);
                    SubTask sub = gson.fromJson(bodyS, SubTask.class);

                    manager.doSubTask(sub);


                    if (manager.getListOfSub().contains(sub)) {
                        h.sendResponseHeaders(200, 0);
                        h.close();
                    } else {

                        h.sendResponseHeaders(500, 0);
                        return;
                    }

                    break;

                case DEL_EPIC:
                    String queryEP = h.getRequestURI().getRawQuery();
                    if (queryEP != null) {

                        String[] id = queryEP.split("=");
                        manager.removeEpicTask(Integer.parseInt(id[1]));
                        if (!manager.getListOfEpic().contains(manager
                                .getEpicTask(Integer.parseInt(id[1])))) {
                            h.sendResponseHeaders(200, 0);
                            h.close();
                        } else {
                            h.sendResponseHeaders(500, 0);
                            return;
                        }
                    }
                    break;

                case DEL_EPICS:

                    manager.clearEpicTasks();

                    if (manager.getListOfEpic().isEmpty()) {
                        h.sendResponseHeaders(200, 0);
                        h.close();
                    } else {
                        h.sendResponseHeaders(500, 0);
                        return;
                    }
                    break;
                case DEL_TASK:
                    String queryT = h.getRequestURI().getRawQuery();
                    if (queryT != null) {

                        String[] id = queryT.split("=");
                        manager.removeTask(Integer.parseInt(id[1]));
                        if (!manager.getListOfTask().contains(manager
                                .getTask(Integer.parseInt(id[1])))) {
                            h.sendResponseHeaders(200, 0);
                            h.close();
                        } else {
                            h.sendResponseHeaders(500, 0);
                            return;
                        }
                    }
                    break;

                case DEL_TASKS:

                    manager.clearTasks();

                    if (manager.getListOfTask().isEmpty()) {
                        h.sendResponseHeaders(200, 0);
                        h.close();
                    } else {
                        h.sendResponseHeaders(500, 0);
                        return;
                    }
                    break;

                case DEL_SUBTASK:
                    String querySub = h.getRequestURI().getRawQuery();
                    if (querySub != null) {

                        String[] id = querySub.split("=");
                        try {
                            manager.removeSubTask(Integer.parseInt(id[1]));
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (!manager.getListOfSub().contains(manager
                                .getSubTask(Integer.parseInt(id[1])))) {
                            h.sendResponseHeaders(200, 0);
                            h.close();
                        } else {
                            h.sendResponseHeaders(500, 0);
                            return;
                        }
                    }
                    break;
                case DEL_SUBTASKS:
                    manager.clearSubTasks();
                    if (manager.getListOfSub().isEmpty()) {
                        h.sendResponseHeaders(200, 0);
                        h.close();
                    } else {
                        h.sendResponseHeaders(500, 0);
                        return;
                    }
                    break;

                case DEL_HISTORY:
                    manager.historyManager.clearHistory();
                    if (manager.historyManager.getHistory().isEmpty()) {
                        h.sendResponseHeaders(200, 0);
                        h.close();
                    } else {
                        h.sendResponseHeaders(500, 0);
                        return;
                    }
                    break;
            }
        }

    }

    public Endpoint getEndpoint(URI uri, String method) {

        String[] slitPath = uri.getPath().split("/");

        Endpoint forReturn = Endpoint.GET_ALL_TASKS;
        if (slitPath.length == 2) {
            return forReturn;
        } else {

            switch (method) {
                case "GET":
                    if (slitPath[2].contains("subtask")) {
                        if (uri.getPath().contains("subtask/epic")) {
                            forReturn = Endpoint.GET_SUB_EPIC;
                        } else {
                            if (uri.getQuery() != null) {
                                forReturn = Endpoint.GET_SUBTASK;
                            } else {
                                forReturn = Endpoint.GET_SUBTASKS;
                            }
                        }


                    } else if (slitPath[2].contains("task")) {

                        if (uri.getQuery() != null) {
                            forReturn = GET_TASK;
                        } else {
                            forReturn = Endpoint.GET_TASKS;
                        }

                    } else if (slitPath[2].contains("epic")) {

                        if (uri.getQuery() != null) {
                            forReturn = Endpoint.GET_EPIC;
                        } else {
                            forReturn = Endpoint.GET_EPICS;
                        }

                    } else if (slitPath[2].contains("history")) {
                        forReturn = Endpoint.GET_HISTORY;
                    }
                    break;

                case "POST":
                    if (slitPath[2].equals("subtask")) {
                        forReturn = Endpoint.POST_SUBTASK;
                    } else if (slitPath[2].equals("task")) {

                        forReturn = Endpoint.POST_TASK;

                    } else if (slitPath[2].equals("epic")) {

                        forReturn = Endpoint.POST_EPIC;

                    }
                    break;

                case "DELETE":

                    if (slitPath[2].contains("subtask")) {
                        if (uri.getQuery() != null) {
                            forReturn = Endpoint.DEL_SUBTASK;
                        } else {
                            forReturn = Endpoint.DEL_SUBTASKS;
                        }

                    } else if (slitPath[2].contains("task")) {
                        if (uri.getQuery() != null) {
                            forReturn = Endpoint.DEL_TASK;
                        } else {
                            forReturn = Endpoint.DEL_TASKS;
                        }

                    } else if (slitPath[2].contains("epic")) {
                        if (uri.getQuery() != null) {
                            forReturn = Endpoint.DEL_EPIC;
                        } else {
                            forReturn = Endpoint.DEL_EPICS;
                        }

                    } else if (slitPath[2].contains("history")) {
                        forReturn = Endpoint.DEL_HISTORY;
                    }
                    break;
                default:
                    break;
            }
        }
        return forReturn;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();


        // код сериализации
    }
}
