package com.ke.mianshiya.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class LongAdderCountManager {

    // 用于存储每个用户的访问次数计数器
    private ConcurrentHashMap<String, LongAdder> userRequestCounts = new ConcurrentHashMap<>();

    // 统计的时间间隔（比如每分钟重置一次计数）
    private final long interval;

    private static class singletonHandler{
        private final static LongAdderCountManager LONG_ADDER_COUNT_MANAGER = new LongAdderCountManager(60);
    }

    /**
     * 通过静态内部类 保证线程安全的情况下获取单例对象
     * @return
     */
    public static LongAdderCountManager getInstance(){
        return singletonHandler.LONG_ADDER_COUNT_MANAGER;
    }

    public LongAdderCountManager(long intervalInSeconds) {
        this.interval = intervalInSeconds;
        startResetTask();
    }



    /**
     * 每次用户访问时调用此方法
     * @param userId 用户的唯一标识符
     */
    public void recordRequest(String userId) {
        // 获取或者初始化用户的访问计数器
        userRequestCounts.computeIfAbsent(userId, key -> new LongAdder()).increment();
    }

    /**
     * 获取用户的当前访问次数
     * @param userId 用户的唯一标识符
     * @return 用户的访问次数
     */
    public long getRequestCount(String userId) {
        return userRequestCounts.getOrDefault(userId, new LongAdder()).sum();
    }

    /**
     * 定期重置每个用户的访问计数器
     */
    private void startResetTask() {
        // 定时任务，每隔指定的时间间隔重置计数
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(interval);  // 等待指定的时间间隔
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                // 重置每个用户的计数器
                userRequestCounts.clear();
            }
        }).start();
    }

}

