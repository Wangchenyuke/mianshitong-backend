package com.ke.mianshiya.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CountContext implements CountStrategy{
    private CountStrategy countStrategy;

    @Value("${count.strategy}")
    private String countStrategyKey;

    @Override
    public long incrAndGetCount(Long userId) {
        CountStrategy strategy = CountStrategyFactory.getStrategy(countStrategyKey);
        long count = strategy.incrAndGetCount(userId);
        return count;
    }
}
