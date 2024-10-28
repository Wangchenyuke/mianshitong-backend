package com.ke.mianshiya.aop;

import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import com.ke.mianshiya.annotation.HotKeyCheck;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 自动热点探测 AOP
 * @author <a href="https://github.com/like">程序员鱼皮</a>
 * @from <a href="https://ke.icu">编程导航知识星球</a>
 */
@Aspect
@Component
public class HotKeyInterceptor {
    /**
     * 执行拦截
     * @param joinPoint
     * @param hotKeyCheck
     * @return
     */
    @Around("@annotation(hotKeyCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, HotKeyCheck hotKeyCheck) throws Throwable {
        //获取传入参数
        Object[] args = joinPoint.getArgs();
        Object id = args[0];
        String keyPrefix = hotKeyCheck.keyPrefix();
        //拼接key
        String key = keyPrefix + id;

        //获取方法返回值类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class returnType = methodSignature.getReturnType();


        //判断是否是hotkey
        if(JdHotKeyStore.isHotKey(key)){
            //获取key的值
            Object value = JdHotKeyStore.get(key);
            //如果设置了值 就可以直接返回缓存值
            if (value != null){
                return returnType.cast(value);
            }
        }
        // 通过权限校验，放行
        Object result = joinPoint.proceed();

        //如果存在hotkey并且没有值 就将返回值存到缓存中
        JdHotKeyStore.smartSet(key,result);
        return result;
    }
}

