package com.ke.mianshiya.strategy;

import com.ke.mianshiya.manager.LongAdderCountManager;

public class LongAdderCountStrategy implements CountStrategy{
    private final LongAdderCountManager longAdderCountManager;

    public LongAdderCountStrategy(){
        //获取Manager单例
        longAdderCountManager = LongAdderCountManager.getInstance();
    }

    @Override
    public long incrAndGetCount(Long userId) {
        longAdderCountManager.recordRequest(String.valueOf(userId));
        long count = longAdderCountManager.getRequestCount(String.valueOf(userId));
        return count;
    }


}
