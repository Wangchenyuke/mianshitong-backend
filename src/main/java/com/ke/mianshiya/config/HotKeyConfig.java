package com.ke.mianshiya.config;

import com.jd.platform.hotkey.client.ClientStarter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * hotkey配置
 */
@Configuration
@ConfigurationProperties(prefix = "hotkey")
@Data
public class HotKeyConfig  {

    /**
     * etcd 服务器完整地址
     */
    private String etcdSever = "http://127.0.0.1:2379";

    /**
     * 应用名称
     */
    private String appName = "app";

    /**
     * 本地缓存key的最大数量
     */
    private int caffeineSize = 1000;

    /**
     * 批量推送key的间隔时间
     */
    private long pushPeriod = 1000L;

    /**
     * 初始化hotkey
     */
    @Bean
    public void initHotKey() {
        ClientStarter.Builder builder = new ClientStarter.Builder();
        ClientStarter starter = builder.setAppName(appName)
                .setCaffeineSize(caffeineSize)
                .setEtcdServer(etcdSever)
                .setPushPeriod(pushPeriod)
                .build();
        starter.startPipeline();
    }
}
