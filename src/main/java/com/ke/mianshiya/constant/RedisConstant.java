package com.ke.mianshiya.constant;

public interface RedisConstant {

    //登录key的统一前缀
    String USER_SIGN_IN_REDIS_KEY_PREFIX = "user:signins";


    static String getUserSignInRedisKey(int year,Long userId){
       return String.format("%s:%s:%s",USER_SIGN_IN_REDIS_KEY_PREFIX,year,userId);
    }
}
