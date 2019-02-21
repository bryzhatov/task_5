package ua.griddynamics.httpserver.pool.factory;

import ua.griddynamics.httpserver.pool.ExecutorThreadPool;
import ua.griddynamics.httpserver.pool.ServerThreadPool;
import ua.griddynamics.httpserver.pool.ThreadPool;
import ua.griddynamics.httpserver.pool.util.ThreadPoolDTO;

public class ThreadPoolFactory {
    private final ThreadPoolDTO threadPoolDTO;

    public ThreadPoolFactory(ThreadPoolDTO threadPoolDTO) {
        this.threadPoolDTO = threadPoolDTO;
    }

    public ThreadPool getPool(String type) {
        switch (type) {
            case "executor":
                return new ExecutorThreadPool(threadPoolDTO);
            case "server":
                return new ServerThreadPool(threadPoolDTO);
            default:
                return new ExecutorThreadPool(threadPoolDTO);
        }
    }
}
