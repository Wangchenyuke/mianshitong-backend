package com.ke.mianshiya.strategy;

import java.util.concurrent.TimeUnit;

public interface CountStrategy {

     long incrAndGetCount(Long userId);

}
