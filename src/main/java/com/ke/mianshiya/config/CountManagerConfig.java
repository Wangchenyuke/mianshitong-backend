package com.ke.mianshiya.config;

import com.ke.mianshiya.manager.LongAdderCountManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountManagerConfig {
    @Bean
    public LongAdderCountManager longAdderCountManager(){
        //实例化为60秒时间间隔
        return new LongAdderCountManager(60);
    }

}
