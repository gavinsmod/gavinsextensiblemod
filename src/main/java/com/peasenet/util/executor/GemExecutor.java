package com.peasenet.util.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class GemExecutor {
    public static ExecutorService executorService;

    private GemExecutor() {
    }

    public static void init() {
        AtomicInteger corePoolSize = new AtomicInteger(1);
        executorService = Executors.newCachedThreadPool((task) -> {
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.setName("gem-executor-" + corePoolSize.getAndIncrement());
            return thread;
        });
    }

    public static void execute(Runnable task) {
        executorService.execute(task);
    }
}
