package controller;

import java.io.File;

public class Managers {

    public static final String PATH_PROJECT_DIR = System.getProperty("user.dir");
    public static final File PATH_FILE = new File(PATH_PROJECT_DIR + File.separator
            + "src" + File.separator + "resources", "tasks.csv");
    public static final String PATH_SERVER = "http://localhost:";

    private static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static HTTPTaskManager httpTaskManager = new HTTPTaskManager(PATH_SERVER + KVServer.PORT);

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }

    public static TaskManager getDefaultTask() {
        return httpTaskManager;
    }
}
