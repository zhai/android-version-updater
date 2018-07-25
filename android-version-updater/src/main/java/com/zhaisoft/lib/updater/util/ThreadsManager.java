package com.zhaisoft.lib.updater.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadsManager {

    private static ExecutorService mExecutorService;
    private static Map<Integer, Future> mTaskMap = new HashMap<>();

    /**
     * Init thread pool.
     */
    public static void init() {
        if (null == mExecutorService) {
            mExecutorService = Executors.newCachedThreadPool();
            clear();
        }
    }

    /**
     * Clear thead pool
     */
    public static void clear() {
        Collection<Future> futures = mTaskMap.values();
        if (futures.size() > 0) {
            for (Future future : futures) {
                stop(future.hashCode());
            }
        }
    }

    /**
     * Performs a task in a thread.
     *
     * @param runnable task runnable
     * @return Task name, use it to stop task.
     */
    public static Integer post(Runnable runnable) {
        if (null == mExecutorService) {
            init();
        }
        Future future = mExecutorService.submit(runnable);
        Integer taskName = future.hashCode();
        mTaskMap.put(future.hashCode(), future);
        return taskName;
    }

    /**
     * Finish the specified task.
     *
     * @param taskName
     */
    public static void stop(Integer taskName) {
        Future future = mTaskMap.get(taskName);
        if (null != future) {
            mTaskMap.remove(taskName);
            if (!future.isDone() && !future.isCancelled() && (null != mExecutorService)) {
                future.cancel(true);
            }
        }
    }
}