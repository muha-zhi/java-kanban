package taskTrecker.tasks.server;

import com.sun.net.httpserver.HttpServer;
import taskTrecker.tasks.manager.FileBackedTasksManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer extends FileBackedTasksManager {


    public static final int PORT = 8080;

    private final HttpServer server;


    public HttpTaskServer() throws IOException, InterruptedException {
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new HttpHandlerTask());

    }

    public void start() {
        System.out.println("Сервер запущен на порту " + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
    }


}


