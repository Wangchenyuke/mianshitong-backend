package com.ke.mianshiya.manager;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.IntegerCodec;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisCountManager {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 增加并返回计数 默认一分钟时间间隔过期时间
     * @param key
     * @return
     */
    public long incrAndGetCount(String key){
        return incrAndGetCount(key,1,TimeUnit.MINUTES,180);
    }

    /**
     * 增加并返回计数 默认一分钟时间间隔
     * @param key
     * @param expirationTimeInSeconds
     * @return
     */
    public long incrAndGetCount(String key, int expirationTimeInSeconds){
        return incrAndGetCount(key,1,TimeUnit.MINUTES,expirationTimeInSeconds);
    }

    /**
     *  增加并返回技术
     * @param key 缓存key
     * @param timeInterval  时间间隔
     * @param timeUnit  时间单位
     * @return
     */
    public long incrAndGetCount(String key, int timeInterval, TimeUnit timeUnit){
        int expirationTimeInSeconds;
        switch (timeUnit){
            case SECONDS:
                expirationTimeInSeconds = timeInterval;
                break;
            case MINUTES:
                expirationTimeInSeconds = timeInterval * 60;
                break;
            case HOURS:
                expirationTimeInSeconds = timeInterval * 3600;
            default:
                throw new IllegalArgumentException("Unsupported TimeUnit,Use SECONDS, MINUTES or HOURS");
        }
        return incrAndGetCount(key,timeInterval,timeUnit,expirationTimeInSeconds);
    }


    /**
     *  增加并返回计数
     * @param key 缓存key
     * @param timeInterval  时间间隔
     * @param timeUnit  时间单位
     * @param expirationTimeInSeconds  过期时间
     * @return
     */
    public long incrAndGetCount(String key, int timeInterval, TimeUnit timeUnit,int expirationTimeInSeconds){
        long timeFactor;
        switch (timeUnit){
            case SECONDS:
                timeFactor = Instant.now().getEpochSecond()/timeInterval;
                break;
            case MINUTES:
                timeFactor = Instant.now().getEpochSecond()/60/timeInterval;
                break;
            case HOURS:
                timeFactor = Instant.now().getEpochSecond()/3600/timeInterval;
                break;
            default:
                throw new IllegalArgumentException("Unsupported TimeUnit,Use SECONDS, MINUTES or HOURS");
        }

        String redisKey = key+":"+timeFactor;

        // Lua 脚本
        String luaScript =
                "if redis.call('exists', KEYS[1]) == 1 then " +
                        "  return redis.call('incr', KEYS[1]); " +
                        "else " +
                        "  redis.call('set', KEYS[1], 1); " +
                        "  redis.call('expire', KEYS[1], ARGV[1]); " +
                        "  return 1; " +
                        "end";

        //执行lua脚本
        RScript script = redissonClient.getScript(IntegerCodec.INSTANCE);
        Object countObj = script.eval(
                RScript.Mode.READ_WRITE,
                luaScript,
                RScript.ReturnType.INTEGER,
                Collections.singletonList(redisKey),
                expirationTimeInSeconds
        );

        return (long) countObj;
    }
}


