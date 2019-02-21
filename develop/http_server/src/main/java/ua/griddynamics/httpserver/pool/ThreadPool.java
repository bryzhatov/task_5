package ua.griddynamics.httpserver.pool;

import ua.griddynamics.httpserver.pool.util.ThreadPoolDTO;
import ua.griddynamics.httpserver.tasks.Task;

import java.util.List;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-01
 */
public abstract class ThreadPool {
    protected final int coreCountThreads;
    protected final int maxCountThreads;
    protected final int timeIdle;

    protected ThreadPool(ThreadPoolDTO threadPoolDTO) {
        coreCountThreads = threadPoolDTO.getCorePoolSize();
        maxCountThreads = threadPoolDTO.getMaxPoolSize();
        timeIdle = threadPoolDTO.getKeepAliveTime();
    }

    public abstract boolean handle(Task task);

    public abstract List<Runnable> shutdownNow();
}
