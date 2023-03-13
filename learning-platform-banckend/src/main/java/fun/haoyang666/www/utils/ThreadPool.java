package fun.haoyang666.www.utils;

import java.util.concurrent.*;

/**
 * @author yang
 * @createTime 2023/1/29 10:48
 * @description
 */

public class ThreadPool {

    private volatile static ExecutorService threadPool;

    private ThreadPool() {
    }

    public static ExecutorService instance() {
        if (threadPool == null) {
            synchronized (ThreadPool.class) {
                if (threadPool == null) {
                    threadPool = new ThreadPoolExecutor(5, 10, 3,
                            TimeUnit.MINUTES, new LinkedBlockingQueue<>(100),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.DiscardOldestPolicy());
                }
            }
        }
        return threadPool;
    }
}
