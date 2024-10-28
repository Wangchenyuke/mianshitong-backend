package com.ke.mianshiya.strategy;

import com.ke.mianshiya.manager.RedisCountManager;
import org.elasticsearch.search.RescoreDocIds;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.IntegerCodec;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class RedisCountStrategy implements CountStrategy{
    @Resource
    private RedissonClient redissonClient;

    /**
     * 增加并计数
     */
    @Override
    public long incrAndGetCount(Long userId) {
        //时间间隔为一分钟
        int timeInterval = 1;
        TimeUnit timeUnit = TimeUnit.MINUTES;
        //过期时间保险设置为180秒
        int expirationTimeInSeconds = 180;
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

        String redisKey = "user:access:"+userId+":"+timeFactor;

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
