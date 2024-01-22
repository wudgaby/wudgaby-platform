package com.wudgaby.platform.core.support;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/2/22 0022 11:06
 * @desc :
 */
public class BasicThreadPoolExecutor {
    private static volatile ThreadPoolExecutor threadPoolExecutor = null;

    /**
     * 核心线程数
     */
    private static final int CORE_POOL_SIZE = 0;

    /**
     * 最大线程数
     */
    private static final int MAX_MUM_POOL_SIZE = 1000;

    /**
     * 线程数空闲存活时间
     */
    private static final long KEEP_ALIVE_TIME = 2;
    private static final TimeUnit UNIT = TimeUnit.MINUTES;

    /**
     * 队列容量
     */
    private static final int CAPACITY = 10;

    /**
     * 拒绝策略. 使用调用线程执行
     */
    private static final RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

    private static final BasicThreadFactory FACTORY = new BasicThreadFactory.Builder().namingPattern("basic-thread-pool-%d").build();

    private BasicThreadPoolExecutor() { }

    public static ThreadPoolExecutor getInstance() {
        if (threadPoolExecutor == null) {
            synchronized (BasicThreadPoolExecutor.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_MUM_POOL_SIZE, KEEP_ALIVE_TIME, UNIT, new ArrayBlockingQueue<>(CAPACITY), FACTORY, HANDLER);
                }
            }
        }
        return threadPoolExecutor;
    }
}
