package com.ke.mianshiya.config;


import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson配置
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {
    private Integer database;
    //地址
    private String host;
    //端口
    private Integer port;
    //密码
    private String password;


    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        //这里不适用集群所以 使用singleServer
        config.useSingleServer()
                .setDatabase(database)
                .setAddress("redis://" + host +":"+ port) //拼接想要连接的地址
                .setPassword(password);

        return Redisson.create(config);
    }
}
