package com.ke.mianshiya.strategy;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;

public class CountStrategyFactory {


    private static final HashMap<String,CountStrategy> countStrategyMap = new HashMap();

    static {
        countStrategyMap.put("LongAdder",new LongAdderCountStrategy());
        countStrategyMap.put("Redis",new RedisCountStrategy());
    }

    public static CountStrategy getStrategy(String countStrategyKey){
        return countStrategyMap.get(countStrategyKey);
    }
}
