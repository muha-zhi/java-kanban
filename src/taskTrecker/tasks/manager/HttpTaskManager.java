package taskTrecker.tasks.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import taskTrecker.history.InMemoryHistoryManager;
import taskTrecker.history.Node;
import taskTrecker.tasks.Epic;
import taskTrecker.tasks.SubTask;
import taskTrecker.tasks.Task;
import taskTrecker.tasks.server.DateAdapter;
import taskTrecker.tasks.server.DurationAdapter;
import taskTrecker.tasks.server.KVTaskClient;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
    @Expose
    private final static String KEY = "key123";

    @Expose
    private KVTaskClient client;




    public void setClient(KVTaskClient client) {
        this.client = client;
    }


    public void startClient() throws IOException, InterruptedException {
        client = new KVTaskClient();
    }


    @Override
    public void save() throws IOException, InterruptedException {
        if (client == null) {
            startClient();
        }
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new DateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        HttpTaskManager manager = new HttpTaskManager();
        manager.setTasks(tasks);
        manager.setSubTasks(subTasks);
        manager.setHistoryManager(historyManager);
        manager.setEpicTasks(epicTasks);
        manager.setSortedTasks(sortedTasks);
        manager.setClient(client);
        manager.setIdOfAll(idOfAll);
        manager.setTaskWithoutDate(taskWithoutDate);

        String JM = gson.toJson(manager);
        client.put(KEY, JM);

    }


    public static HttpTaskManager load(String key) throws IOException {

        Gson gson1 = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new DateAdapter())
                .serializeNulls()
                .create();
        try {
            KVTaskClient client1 = new KVTaskClient();


            HttpTaskManager manager = gson1.fromJson(client1.load(key), HttpTaskManager.class);


            if (manager != null) {
                if (manager.getListOfEpic() != null) {
                    for (Epic epic : manager.getListOfEpic()) {
                        if (manager.getListOfSub() != null) {
                            for (SubTask subTask : manager.getListOfSub()) {
                                if (epic != null && subTask != null) {
                                    if (epic.getSubsOfThisEpic() != null && epic.getSortedSubsOfThisEpic() != null) {
                                        if (subTask.getEpicObject() == epic.getId()) {
                                            epic.addSubForEpic(subTask);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            List<Task> tasks = new ArrayList<>();
            if (manager != null) {
                Map<Integer, Node<Task>> t = manager.historyManager.getTasksToRemove();
                if (t != null) {
                    for (Integer k : t.keySet()) {

                        Task task1 = t.get(k).getData();
                        if (task1 != null) {
                            tasks.add(task1);
                        }

                    }
                }

                InMemoryHistoryManager historyManager1 = new InMemoryHistoryManager();
                for (Task val : tasks) {
                    historyManager1.add(val);
                }
                manager.setHistoryManager(historyManager1);
            }


            return manager;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }


}
