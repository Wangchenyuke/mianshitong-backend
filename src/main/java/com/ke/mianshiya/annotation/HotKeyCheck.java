package com.ke.mianshiya.annotation;

import org.apache.poi.ss.formula.functions.T;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 热key探测
 *
 * @author <a href="https://github.com/like">程序员鱼皮</a>
 * @from <a href="https://ke.icu">编程导航知识星球</a>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HotKeyCheck {

    /**
     * key的前缀
     *
     * @return
     */
    String keyPrefix() default "";


}

