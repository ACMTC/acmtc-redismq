package com.acmtc.redisMQ.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisMQInitialPool
 * @Description TODO
 * @Author paul
 * @Date 2018/9/29 18:54
 * @Version 1.0
 */
public class RedisMQInitialPool {
    //线程池中控制Running状态的线程为100个
    public static ExecutorService pool = new ThreadPoolExecutor(100, 100, 2L, TimeUnit.MINUTES,
            new LinkedBlockingQueue<Runnable>());
}
